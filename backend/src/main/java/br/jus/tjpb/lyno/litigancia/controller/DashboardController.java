package br.jus.tjpb.lyno.litigancia.controller;

import br.jus.tjpb.lyno.litigancia.dto.DashboardDtos.*;
import br.jus.tjpb.lyno.litigancia.service.DenunciaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DenunciaService service;

    public DashboardController(DenunciaService service) {
        this.service = service;
    }

    @GetMapping("/kpis")
    public KpisResponse kpis() {
        return service.kpis();
    }

    @GetMapping("/ranking")
    public List<RankingItem> ranking() {
        return service.ranking();
    }

    @GetMapping("/tipologia")
    public List<TipologiaItem> tipologia() {
        return service.distribuicaoTipologia();
    }

    @GetMapping("/comarcas")
    public List<ComarcaItem> comarcas() {
        return service.distribuicaoComarcas();
    }

    @GetMapping("/tendencia")
    public List<TendenciaItem> tendencia() {
        return service.tendenciaSemanal();
    }
}
