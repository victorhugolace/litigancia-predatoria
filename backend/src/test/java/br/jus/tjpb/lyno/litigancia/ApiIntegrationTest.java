package br.jus.tjpb.lyno.litigancia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import br.jus.tjpb.lyno.litigancia.repository.DenunciaRepository;
import br.jus.tjpb.lyno.litigancia.service.DenunciaService;
import br.jus.tjpb.lyno.litigancia.dto.DenunciaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.security.test.context.support.WithMockUser(username = "analista", roles = "ANALISTA")
class ApiIntegrationTest {
    @org.springframework.test.context.DynamicPropertySource
    static void exigirBancoDeTeste(org.springframework.test.context.DynamicPropertyRegistry registry) {
        if (!"litigancia_test".equals(System.getenv("DB_NAME"))) {
            throw new IllegalStateException("Execute a suíte com compose.test.yml: DB_NAME deve ser litigancia_test");
        }
    }
    @Autowired MockMvc mvc;
    @Autowired DenunciaRepository repository;
    @Autowired DenunciaService service;
    @Autowired ObjectMapper mapper;
    @Autowired org.springframework.jdbc.core.JdbcTemplate jdbc;

    @BeforeEach
    void limparBancoIsolado() { repository.deleteAll(); }

    static String dadosValidos() {
        return """
            {"comarca":" João Pessoa ","nomeComunicante":" Juíza Teste ",
             "matriculaComunicante":" MAT1 ","oabAdvogado":"pb / 12345",
             "cpfCnpjParte":"529.982.247-25", "tipologia":"FATIAMENTO_PEDIDOS",
             "processosParadigmas":["1234567-89.2026.8.15.0001","1234568-89.2026.8.15.0001","1234569-89.2026.8.15.0001"],
             "relato":" Padrão repetido em três processos. ","anexos":[]}
            """;
    }

    @Test
    void iniciaComMigracoesEAgregacoesPostgres() throws Exception {
        mvc.perform(get("/api/dashboard/kpis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDenuncias").value(0));
        mvc.perform(get("/api/dashboard/tendencia"))
                .andExpect(status().isOk());
        mvc.perform(post("/api/denuncias").with(csrf()).contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.oabAdvogado").exists());
    }

    @Test
    void cadastraNormalizaEBuscaColecoesForaDaTransacao() throws Exception {
        String body = mvc.perform(post("/api/denuncias").with(csrf()).contentType("application/json").content(dadosValidos()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comarca").value("João Pessoa"))
                .andExpect(jsonPath("$.oabAdvogado").value("PB12345"))
                .andExpect(jsonPath("$.cpfCnpjParte").value("52998224725"))
                .andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(body).get("id").asLong();
        mvc.perform(get("/api/denuncias/" + id))
                .andExpect(status().isOk()).andExpect(jsonPath("$.processosParadigmas.length()").value(3));
        mvc.perform(patch("/api/denuncias/" + id + "/status").with(csrf()).contentType("application/json")
                .content("{\"volumeProcessualEstimado\":-1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaEvidenciasDuplicadasNulasEDocumentosInvalidos() throws Exception {
        for (String invalid : new String[] {
                dadosValidos().replace("1234568-89", "1234567-89"),
                dadosValidos().replace("\"1234568-89.2026.8.15.0001\"", "null"),
                dadosValidos().replace("529.982.247-25", "111.111.111-11"),
                dadosValidos().replace("FATIAMENTO_PEDIDOS", "OUTROS"),
                dadosValidos().replace("João Pessoa", "X".repeat(121))
        }) {
            mvc.perform(post("/api/denuncias").with(csrf()).contentType("application/json").content(invalid))
                    .andExpect(status().isBadRequest());
        }
        assertThat(repository.count()).isZero();
    }

    @Test
    void aceitaCnpjNumericoEAlfanumericoSemPerderLetras() throws Exception {
        for (var exemplo : java.util.Map.of("11.222.333/0001-81", "11222333000181",
                "12.abc.345/01de-35", "12ABC34501DE35", "00.000.000/e08g-12", "00000000E08G12").entrySet()) {
            mvc.perform(post("/api/denuncias").with(csrf()).contentType("application/json")
                            .content(dadosValidos().replace("529.982.247-25", exemplo.getKey())))
                    .andExpect(status().isCreated()).andExpect(jsonPath("$.cpfCnpjParte").value(exemplo.getValue()));
        }
        for (String invalido : java.util.List.of("12.ABC.345/01DE-36", "12.ABC.345/01DE-3A", "@2.ABC.345/01DE-35")) {
            mvc.perform(post("/api/denuncias").with(csrf()).contentType("application/json")
                            .content(dadosValidos().replace("529.982.247-25", invalido)))
                    .andExpect(status().isBadRequest());
        }
        assertThat(repository.count()).isEqualTo(3);
        mvc.perform(get("/api/dashboard/ranking").param("porDocumento", "true"))
                .andExpect(jsonPath("$[0].identificador").value("00000000E08G12"));
    }

    @Test
    void geraProtocolosUnicosEmEnviosSimultaneos() throws Exception {
        DenunciaRequest request = mapper.readValue(dadosValidos(), DenunciaRequest.class);
        try (var executor = Executors.newFixedThreadPool(6)) {
            var futures = IntStream.range(0, 24).mapToObj(i ->
                    executor.submit(() -> service.criar(request, "analista").getNumeroProtocolo())).toList();
            var protocols = new java.util.HashSet<String>();
            for (var future : futures) protocols.add(future.get(30, TimeUnit.SECONDS));
            assertThat(protocols).hasSize(24);
            assertThat(repository.count()).isEqualTo(24);
            mvc.perform(get("/api/denuncias").param("tamanho", "5").param("pagina", "1"))
                    .andExpect(jsonPath("$.itens.length()").value(5))
                    .andExpect(jsonPath("$.total").value(24))
                    .andExpect(jsonPath("$.totalPaginas").value(5));
        }
    }

    @Test
    void filtraPeriodoECompletaSemanasSemAlertas() throws Exception {
        var request = mapper.readValue(dadosValidos(), DenunciaRequest.class);
        var atual = service.criar(request, "analista");
        var antigo = service.criar(request, "analista");
        jdbc.update("update denuncias set data_criacao = ? where id = ?",
                java.time.LocalDate.now().minusYears(2).atStartOfDay(), antigo.getId());
        mvc.perform(get("/api/dashboard/kpis").param("periodo", "ANO"))
                .andExpect(jsonPath("$.totalDenuncias").value(1));
        mvc.perform(get("/api/dashboard/kpis").param("periodo", "TODOS"))
                .andExpect(jsonPath("$.totalDenuncias").value(2));
        mvc.perform(get("/api/dashboard/tendencia"))
                .andExpect(jsonPath("$.length()").value(12))
                .andExpect(jsonPath("$[0].total").value(0))
                .andExpect(jsonPath("$[11].total").value(1));
        mvc.perform(get("/api/dashboard/ranking").param("porDocumento", "true"))
                .andExpect(jsonPath("$[0].identificador").value("52998224725"))
                .andExpect(jsonPath("$[0].totalDenuncias").value(1));
        mvc.perform(get("/api/dashboard/kpis").param("periodo", "INVALIDO"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizaEtapaAuditaERejeitaVersaoDesatualizada() throws Exception {
        var d = service.criar(mapper.readValue(dadosValidos(), DenunciaRequest.class), "analista");
        String payload = "{\"versao\":0,\"status\":\"EM_ANALISE_NUMOPED\",\"volumeProcessualEstimado\":100,\"comarcasAtingidas\":2,\"notaTecnicaResumo\":\"Em análise\"}";
        mvc.perform(patch("/api/denuncias/" + d.getId() + "/status").with(csrf()).contentType("application/json").content(payload))
                .andExpect(status().isOk()).andExpect(jsonPath("$.versao").value(1))
                .andExpect(jsonPath("$.status").value("EM_ANALISE_NUMOPED"))
                .andExpect(jsonPath("$.historico.length()").value(2));
        mvc.perform(patch("/api/denuncias/" + d.getId() + "/status").with(csrf()).contentType("application/json").content(payload))
                .andExpect(status().isConflict());
        mvc.perform(patch("/api/denuncias/" + d.getId() + "/status").with(csrf()).contentType("application/json")
                        .content("{\"versao\":1,\"limparVolume\":true,\"limparComarcas\":true,\"notaTecnicaResumo\":\"\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.volumeProcessualEstimado").isEmpty())
                .andExpect(jsonPath("$.historico.length()").value(3));
        mvc.perform(get("/api/denuncias").param("tamanho", "101")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/denuncias").param("pagina", "-1")).andExpect(status().isBadRequest());
    }

    @Test
    void salvaPdfEBaixaConteudoSemPermitirTrocaDaDenuncia() throws Exception {
        byte[] pdf = "%PDF-1.4\nDocumento de teste\n%%EOF".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        var dados = new org.springframework.mock.web.MockMultipartFile("dados", "", "application/json", dadosValidos().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        var arquivo = new org.springframework.mock.web.MockMultipartFile("arquivos", "../../laudo.pdf", "application/pdf", pdf);
        var response = mvc.perform(multipart("/api/denuncias").file(dados).file(arquivo).with(csrf()))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.documentos[0].nome").value("laudo.pdf"))
                .andReturn().getResponse().getContentAsString();
        var tree = mapper.readTree(response);
        long id = tree.get("id").asLong();
        String documento = tree.get("documentos").get(0).get("id").asText();
        mvc.perform(get("/api/denuncias/" + id + "/documentos/" + documento))
                .andExpect(status().isOk()).andExpect(content().bytes(pdf))
                .andExpect(header().string("Cache-Control", "no-store"));
        mvc.perform(get("/api/denuncias/" + (id + 1) + "/documentos/" + documento))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaAnexosInvalidosSemCriarDenunciaParcial() throws Exception {
        var dados = new org.springframework.mock.web.MockMultipartFile("dados", "", "application/json", dadosValidos().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        var falso = new org.springframework.mock.web.MockMultipartFile("arquivos", "falso.pdf", "application/pdf", "nao e PDF".getBytes());
        mvc.perform(multipart("/api/denuncias").file(dados).file(falso).with(csrf())).andExpect(status().isBadRequest());
        var grande = new org.springframework.mock.web.MockMultipartFile("arquivos", "grande.pdf", "application/pdf", new byte[10 * 1024 * 1024 + 1]);
        mvc.perform(multipart("/api/denuncias").file(dados).file(grande).with(csrf())).andExpect(status().isBadRequest());
        mvc.perform(post("/api/denuncias").with(csrf()).contentType("application/json").content(dadosValidos().replace("\"anexos\":[]", "\"anexos\":[\"perdido.pdf\"]")))
                .andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test
    @org.springframework.security.test.context.support.WithAnonymousUser
    void exigeLoginECsrf() throws Exception {
        mvc.perform(get("/api/denuncias")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/dashboard/kpis")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/denuncias").with(user("analista").roles("ANALISTA"))
                .contentType("application/json").content(dadosValidos())).andExpect(status().isForbidden());
    }

    @Test
    void restringeComunicanteAosPropriosCasosEAuditaAutor() throws Exception {
        var propria = service.criar(mapper.readValue(dadosValidos(), DenunciaRequest.class), "comunicante");
        var alheia = service.criar(mapper.readValue(dadosValidos(), DenunciaRequest.class), "outro");
        mvc.perform(get("/api/denuncias/" + propria.getId()).with(user("comunicante").roles("COMUNICANTE")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.historico[0].autor").value("comunicante"));
        mvc.perform(get("/api/denuncias/protocolo/" + propria.getNumeroProtocolo()).with(user("comunicante").roles("COMUNICANTE")))
                .andExpect(status().isOk());
        mvc.perform(get("/api/denuncias/" + alheia.getId()).with(user("comunicante").roles("COMUNICANTE")))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/dashboard/kpis").with(user("comunicante").roles("COMUNICANTE"))).andExpect(status().isForbidden());
        mvc.perform(get("/api/denuncias").with(user("comunicante").roles("COMUNICANTE"))).andExpect(status().isForbidden());
        mvc.perform(patch("/api/denuncias/" + propria.getId() + "/status").with(user("comunicante").roles("COMUNICANTE"))
                        .with(csrf()).contentType("application/json").content("{\"versao\":0,\"status\":\"ARQUIVADO\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @org.springframework.security.test.context.support.WithAnonymousUser
    void autenticaComSessaoRenovaCsrfEEncerraAcesso() throws Exception {
        var inicial = mvc.perform(get("/api/session")).andExpect(status().isOk()).andReturn();
        var session = (org.springframework.mock.web.MockHttpSession) inicial.getRequest().getSession(false);
        String csrfToken = mapper.readTree(inicial.getResponse().getContentAsString()).get("csrfToken").asText();
        // A partir daqui, exercita o contexto real salvo na sessão pelo login.
        org.springframework.security.test.context.TestSecurityContextHolder.clearContext();
        mvc.perform(post("/api/login").session(session).header("X-CSRF-TOKEN", csrfToken)
                        .param("username", "analista").param("password", "senha-teste-analista"))
                .andExpect(status().isNoContent());
        var autenticada = mvc.perform(get("/api/session").session(session)).andExpect(jsonPath("$.autenticado").value(true)).andReturn();
        String novoCsrf = mapper.readTree(autenticada.getResponse().getContentAsString()).get("csrfToken").asText();
        mvc.perform(post("/api/denuncias").session(session).header("X-CSRF-TOKEN", novoCsrf)
                        .contentType("application/json").content(dadosValidos()))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/logout").session(session).header("X-CSRF-TOKEN", novoCsrf)).andExpect(status().isNoContent());
        mvc.perform(get("/api/denuncias")).andExpect(status().isUnauthorized());
    }
}
