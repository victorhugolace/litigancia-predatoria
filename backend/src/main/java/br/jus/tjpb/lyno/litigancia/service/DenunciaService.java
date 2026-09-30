package br.jus.tjpb.lyno.litigancia.service;

import br.jus.tjpb.lyno.litigancia.dto.DashboardDtos.*;
import br.jus.tjpb.lyno.litigancia.dto.DenunciaRequest;
import br.jus.tjpb.lyno.litigancia.dto.DenunciaResponse;
import br.jus.tjpb.lyno.litigancia.model.Denuncia;
import br.jus.tjpb.lyno.litigancia.model.StatusDenuncia;
import br.jus.tjpb.lyno.litigancia.repository.DenunciaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Clock;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DenunciaService {

    private final DenunciaRepository repository;
    private final Clock clock;

    public DenunciaService(DenunciaRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public DenunciaResponse criar(DenunciaRequest request) {
        Denuncia d = new Denuncia();
        d.setComarca(request.getComarca());
        d.setNomeComunicante(request.getNomeComunicante());
        d.setMatriculaComunicante(request.getMatriculaComunicante());
        d.setOabAdvogado(request.getOabAdvogado());
        d.setCpfCnpjParte(request.getCpfCnpjParte());
        d.setTipologia(request.getTipologia());
        d.setTipologiaOutros(request.getTipologiaOutros());
        d.setProcessosParadigmas(request.getProcessosParadigmas());
        d.setRelato(request.getRelato());
        d.setAnexos(request.getAnexos() != null ? request.getAnexos() : List.of());
        d.setStatus(StatusDenuncia.NOVO);
        d.setNumeroProtocolo(gerarNumeroProtocolo());

        Denuncia salvo = repository.save(d);
        return DenunciaResponse.from(salvo);
    }

    @Transactional(readOnly = true)
    public List<DenunciaResponse> listar(StatusDenuncia status) {
        List<Denuncia> denuncias = (status != null)
                ? repository.findByStatusOrderByDataCriacaoDesc(status)
                : repository.findAllByOrderByDataCriacaoDesc();
        return denuncias.stream().map(DenunciaResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DenunciaResponse buscar(Long id) {
        Denuncia d = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrada: " + id));
        return DenunciaResponse.from(d);
    }

    @Transactional
    public DenunciaResponse atualizarStatus(Long id, StatusUpdateRequest request) {
        Denuncia d = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrada: " + id));
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
            d.setNotaTecnicaResumo(request.notaTecnicaResumo);
        }
        return DenunciaResponse.from(repository.save(d));
    }

    private String gerarNumeroProtocolo() {
        long sequencial = repository.proximoProtocolo();
        return "LP-" + Year.now(clock).getValue() + "-" + String.format("%06d", sequencial);
    }

    // --- Dashboard ---

    @Transactional(readOnly = true)
    public KpisResponse kpis() {
        return new KpisResponse(
                repository.countTotal(),
                repository.somaVolumeProcessual(),
                repository.countComarcasDistintas()
        );
    }

    @Transactional(readOnly = true)
    public List<RankingItem> ranking() {
        return repository.rankingReincidencia().stream()
                .limit(10)
                .map(p -> new RankingItem(p.getChave(), p.getTotal(), p.getComarcas()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipologiaItem> distribuicaoTipologia() {
        long total = repository.countTotal();
        return repository.contagemPorTipologia().stream()
                .map(p -> new TipologiaItem(
                        p.getTipologia().name(),
                        p.getTipologia().getDescricao(),
                        p.getTotal(),
                        total == 0 ? 0.0 : Math.round((p.getTotal() * 1000.0 / total)) / 10.0
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComarcaItem> distribuicaoComarcas() {
        return repository.contagemPorComarca().stream()
                .map(p -> new ComarcaItem(p.getComarca(), p.getTotal()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TendenciaItem> tendenciaSemanal() {
        LocalDateTime desde = LocalDateTime.now().minusWeeks(12);
        return repository.tendenciaSemanal(desde).stream()
                .map(p -> new TendenciaItem(p.getSemana().toLocalDate(), p.getTotal()))
                .collect(Collectors.toList());
    }
}
