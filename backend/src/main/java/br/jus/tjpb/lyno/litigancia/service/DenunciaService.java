package br.jus.tjpb.lyno.litigancia.service;

import br.jus.tjpb.lyno.litigancia.dto.DashboardDtos.*;
import br.jus.tjpb.lyno.litigancia.dto.DenunciaRequest;
import br.jus.tjpb.lyno.litigancia.dto.DenunciaResponse;
import br.jus.tjpb.lyno.litigancia.model.Denuncia;
import br.jus.tjpb.lyno.litigancia.model.StatusDenuncia;
import br.jus.tjpb.lyno.litigancia.model.Periodo;
import br.jus.tjpb.lyno.litigancia.repository.DenunciaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Clock;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import org.springframework.data.domain.PageRequest;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DenunciaService {

    private final DenunciaRepository repository;
    private final Clock clock;
    private final br.jus.tjpb.lyno.litigancia.repository.HistoricoRepository historicoRepository;
    private final DocumentoService documentoService;
    private final br.jus.tjpb.lyno.litigancia.repository.DocumentoRepository documentoRepository;

    public DenunciaService(DenunciaRepository repository, Clock clock,
                          br.jus.tjpb.lyno.litigancia.repository.HistoricoRepository historicoRepository,
                          DocumentoService documentoService, br.jus.tjpb.lyno.litigancia.repository.DocumentoRepository documentoRepository) {
        this.repository = repository;
        this.clock = clock;
        this.historicoRepository = historicoRepository;
        this.documentoService = documentoService;
        this.documentoRepository = documentoRepository;
    }

    @Transactional
    public DenunciaResponse criarComDocumentos(DenunciaRequest request, org.springframework.web.multipart.MultipartFile[] arquivos, String usuario) {
        var validados = documentoService.validar(arquivos);
        var response = criar(request, usuario);
        documentoService.salvar(response.getId(), validados);
        return buscar(response.getId());
    }

    @Transactional
    public DenunciaResponse criar(DenunciaRequest request, String usuario) {
        Denuncia d = new Denuncia();
        d.setCriadoPor(usuario);
        d.setComarca(request.getComarca());
        d.setNomeComunicante(request.getNomeComunicante());
        d.setMatriculaComunicante(request.getMatriculaComunicante());
        d.setOabAdvogado(request.getOabAdvogado());
        d.setCpfCnpjParte(request.getCpfCnpjParte());
        d.setTipologia(request.getTipologia());
        d.setTipologiaOutros(request.getTipologia() == br.jus.tjpb.lyno.litigancia.model.Tipologia.OUTROS ? request.getTipologiaOutros() : null);
        d.setProcessosParadigmas(request.getProcessosParadigmas());
        d.setRelato(request.getRelato());
        d.setAnexos(request.getAnexos() != null ? request.getAnexos() : List.of());
        d.setStatus(StatusDenuncia.NOVO);
        d.setNumeroProtocolo(gerarNumeroProtocolo());
        d.setDataCriacao(LocalDateTime.now(clock));
        d.setDataAtualizacao(d.getDataCriacao());

        Denuncia salvo = repository.save(d);
        historicoRepository.save(new br.jus.tjpb.lyno.litigancia.model.Historico(salvo, null, usuario, salvo.getDataCriacao()));
        return DenunciaResponse.from(salvo);
    }

    @Transactional(readOnly = true)
    public br.jus.tjpb.lyno.litigancia.dto.PaginaDenuncias listar(StatusDenuncia status, int pagina, int tamanho) {
        if (pagina < 0 || tamanho < 1 || tamanho > 100) throw new IllegalArgumentException("Página inválida ou tamanho fora de 1 a 100");
        var page = PageRequest.of(pagina, tamanho);
        var denuncias = status != null ? repository.findByStatusOrderByDataCriacaoDescIdDesc(status, page)
                : repository.findAllByOrderByDataCriacaoDescIdDesc(page);
        var totais = new java.util.EnumMap<StatusDenuncia, Long>(StatusDenuncia.class);
        for (var etapa : StatusDenuncia.values()) totais.put(etapa, repository.countByStatus(etapa));
        return new br.jus.tjpb.lyno.litigancia.dto.PaginaDenuncias(denuncias.map(DenunciaResponse::from).getContent(),
                denuncias.getTotalElements(), pagina, denuncias.getTotalPages(), totais);
    }

    @Transactional(readOnly = true)
    public DenunciaResponse buscar(Long id) {
        Denuncia d = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrada: " + id));
        var response = DenunciaResponse.from(d);
        response.setHistorico(historicoRepository.findByDenunciaIdOrderByIdDesc(id));
        response.setDocumentos(documentoRepository.listarMetadados(id));
        return response;
    }

    @Transactional
    public DenunciaResponse atualizarStatus(Long id, StatusUpdateRequest request, String usuario) {
        Denuncia d = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrada: " + id));
        if (!java.util.Objects.equals(d.getVersao(), request.versao)) {
            throw new org.springframework.orm.ObjectOptimisticLockingFailureException(Denuncia.class, id);
        }
        if ((request.limparVolume && request.volumeProcessualEstimado != null) ||
                (request.limparComarcas && request.comarcasAtingidas != null)) {
            throw new IllegalArgumentException("Não informe e limpe a mesma estimativa na mesma requisição");
        }
        var anterior = d.getStatus();
        if (request.status != null) {
            d.setStatus(request.status);
        }
        if (request.volumeProcessualEstimado != null) {
            d.setVolumeProcessualEstimado(request.volumeProcessualEstimado);
        }
        if (request.comarcasAtingidas != null) {
            d.setComarcasAtingidas(request.comarcasAtingidas);
        }
        if (request.notaTecnicaResumo != null) {
            d.setNotaTecnicaResumo(request.notaTecnicaResumo.strip());
        }
        if (request.limparVolume) d.setVolumeProcessualEstimado(null);
        if (request.limparComarcas) d.setComarcasAtingidas(null);
        d.setDataAtualizacao(LocalDateTime.now(clock));
        repository.saveAndFlush(d);
        historicoRepository.save(new br.jus.tjpb.lyno.litigancia.model.Historico(d, anterior, usuario, d.getDataAtualizacao()));
        return buscar(id);
    }

    @Transactional(readOnly = true)
    public void verificarAcesso(Long id, org.springframework.security.core.Authentication auth) {
        var denuncia = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrada"));
        boolean analista = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANALISTA"));
        if (!analista && (auth == null || !auth.getName().equals(denuncia.getCriadoPor()))) {
            throw new EntityNotFoundException("Denúncia não encontrada");
        }
    }

    @Transactional(readOnly = true)
    public DenunciaResponse buscarPorProtocolo(String protocolo, org.springframework.security.core.Authentication auth) {
        var denuncia = repository.findByNumeroProtocolo(protocolo.strip().toUpperCase(java.util.Locale.ROOT))
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrada"));
        verificarAcesso(denuncia.getId(), auth);
        return buscar(denuncia.getId());
    }

    private String gerarNumeroProtocolo() {
        long sequencial = repository.proximoProtocolo();
        return "LP-" + Year.now(clock).getValue() + "-" + String.format("%06d", sequencial);
    }

    // --- Dashboard ---

    @Transactional(readOnly = true)
    public KpisResponse kpis(Periodo periodo) {
        LocalDateTime desde = periodo.inicio(LocalDate.now(clock)), ate = LocalDateTime.now(clock);
        return new KpisResponse(
                repository.countTotal(desde, ate),
                repository.somaVolumeProcessual(desde, ate),
                repository.countComarcasDistintas(desde, ate)
        );
    }

    @Transactional(readOnly = true)
    public List<RankingItem> ranking(Periodo periodo, boolean porDocumento) {
        LocalDateTime desde = periodo.inicio(LocalDate.now(clock)), ate = LocalDateTime.now(clock);
        var pagina = PageRequest.of(0, 10);
        var dados = porDocumento ? repository.rankingDocumentos(desde, ate, pagina) : repository.rankingReincidencia(desde, ate, pagina);
        return dados.stream()
                .map(p -> new RankingItem(p.getChave(), p.getTotal(), p.getComarcas()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipologiaItem> distribuicaoTipologia(Periodo periodo) {
        LocalDateTime desde = periodo.inicio(LocalDate.now(clock)), ate = LocalDateTime.now(clock);
        long total = repository.countTotal(desde, ate);
        return repository.contagemPorTipologia(desde, ate).stream()
                .map(p -> new TipologiaItem(
                        p.getTipologia().name(),
                        p.getTipologia().getDescricao(),
                        p.getTotal(),
                        total == 0 ? 0.0 : Math.round((p.getTotal() * 1000.0 / total)) / 10.0
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComarcaItem> distribuicaoComarcas(Periodo periodo) {
        LocalDateTime desde = periodo.inicio(LocalDate.now(clock)), ate = LocalDateTime.now(clock);
        return repository.contagemPorComarca(desde, ate).stream()
                .map(p -> new ComarcaItem(p.getComarca(), p.getTotal()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TendenciaItem> tendenciaSemanal(Periodo periodo) {
        LocalDate inicio = LocalDate.now(clock).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(11);
        LocalDateTime desdePeriodo = periodo.inicio(LocalDate.now(clock));
        LocalDateTime desde = desdePeriodo.isAfter(inicio.atStartOfDay()) ? desdePeriodo : inicio.atStartOfDay();
        var contagens = repository.tendenciaSemanal(desde, LocalDateTime.now(clock)).stream()
                .collect(Collectors.toMap(p -> p.getSemana().toLocalDate(), p -> p.getTotal()));
        return java.util.stream.IntStream.range(0, 12)
                .mapToObj(i -> new TendenciaItem(inicio.plusWeeks(i), contagens.getOrDefault(inicio.plusWeeks(i), 0L)))
                .toList();
    }
}
