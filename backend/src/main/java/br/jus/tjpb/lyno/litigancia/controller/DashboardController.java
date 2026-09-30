package br.jus.tjpb.lyno.litigancia.controller;

import br.jus.tjpb.lyno.litigancia.dto.DashboardDtos.*;
import br.jus.tjpb.lyno.litigancia.service.DenunciaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import br.jus.tjpb.lyno.litigancia.model.Periodo;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DenunciaService service;

    public DashboardController(DenunciaService service) {
        this.service = service;
    }

    @GetMapping("/kpis")
    public KpisResponse kpis(@RequestParam(defaultValue = "ANO") Periodo periodo) {
        return service.kpis(periodo);
    }

    @GetMapping("/ranking")
    public List<RankingItem> ranking(@RequestParam(defaultValue = "ANO") Periodo periodo,
                                    @RequestParam(defaultValue = "false") boolean porDocumento) {
        return service.ranking(periodo, porDocumento);
    }

    @GetMapping("/tipologia")
    public List<TipologiaItem> tipologia(@RequestParam(defaultValue = "ANO") Periodo periodo) {
        return service.distribuicaoTipologia(periodo);
    }

    @GetMapping("/comarcas")
    public List<ComarcaItem> comarcas(@RequestParam(defaultValue = "ANO") Periodo periodo) {
        return service.distribuicaoComarcas(periodo);
    }

    @GetMapping("/tendencia")
    public List<TendenciaItem> tendencia(@RequestParam(defaultValue = "ANO") Periodo periodo) {
        return service.tendenciaSemanal(periodo);
    }
}
