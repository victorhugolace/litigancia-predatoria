package br.jus.tjpb.lyno.litigancia.controller;

import br.jus.tjpb.lyno.litigancia.dto.DashboardDtos.StatusUpdateRequest;
import br.jus.tjpb.lyno.litigancia.dto.DenunciaRequest;
import br.jus.tjpb.lyno.litigancia.dto.DenunciaResponse;
import br.jus.tjpb.lyno.litigancia.model.StatusDenuncia;
import br.jus.tjpb.lyno.litigancia.service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    private final DenunciaService service;

    public DenunciaController(DenunciaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DenunciaResponse> criar(@Valid @RequestBody DenunciaRequest request) {
        DenunciaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public br.jus.tjpb.lyno.litigancia.dto.PaginaDenuncias listar(@RequestParam(required = false) StatusDenuncia status,
            @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "25") int tamanho) {
        return service.listar(status, pagina, tamanho);
    }

    @GetMapping("/{id}")
    public DenunciaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PatchMapping("/{id}/status")
    public DenunciaResponse atualizarStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return service.atualizarStatus(id, request);
    }
}
