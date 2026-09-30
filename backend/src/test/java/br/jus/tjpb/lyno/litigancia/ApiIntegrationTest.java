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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired DenunciaRepository repository;
    @Autowired DenunciaService service;
    @Autowired ObjectMapper mapper;

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
        mvc.perform(post("/api/denuncias").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.oabAdvogado").exists());
    }

    @Test
    void cadastraNormalizaEBuscaColecoesForaDaTransacao() throws Exception {
        String body = mvc.perform(post("/api/denuncias").contentType("application/json").content(dadosValidos()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comarca").value("João Pessoa"))
                .andExpect(jsonPath("$.oabAdvogado").value("PB12345"))
                .andExpect(jsonPath("$.cpfCnpjParte").value("52998224725"))
                .andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(body).get("id").asLong();
        mvc.perform(get("/api/denuncias/" + id))
                .andExpect(status().isOk()).andExpect(jsonPath("$.processosParadigmas.length()").value(3));
        mvc.perform(patch("/api/denuncias/" + id + "/status").contentType("application/json")
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
            mvc.perform(post("/api/denuncias").contentType("application/json").content(invalid))
                    .andExpect(status().isBadRequest());
        }
        assertThat(repository.count()).isZero();
    }

    @Test
    void geraProtocolosUnicosEmEnviosSimultaneos() throws Exception {
        DenunciaRequest request = mapper.readValue(dadosValidos(), DenunciaRequest.class);
        try (var executor = Executors.newFixedThreadPool(6)) {
            var futures = IntStream.range(0, 24).mapToObj(i ->
                    executor.submit(() -> service.criar(request).getNumeroProtocolo())).toList();
            var protocols = new java.util.HashSet<String>();
            for (var future : futures) protocols.add(future.get(30, TimeUnit.SECONDS));
            assertThat(protocols).hasSize(24);
            assertThat(repository.count()).isEqualTo(24);
        }
    }
}
