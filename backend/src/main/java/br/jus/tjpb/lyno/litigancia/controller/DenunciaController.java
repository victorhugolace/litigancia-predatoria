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
    private final br.jus.tjpb.lyno.litigancia.service.DocumentoService documentoService;

    public DenunciaController(DenunciaService service, br.jus.tjpb.lyno.litigancia.service.DocumentoService documentoService) {
        this.service = service;
        this.documentoService = documentoService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<DenunciaResponse> criar(@Valid @RequestBody DenunciaRequest request, org.springframework.security.core.Authentication auth) {
        DenunciaResponse response = service.criar(request, autor(auth));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DenunciaResponse> criarComDocumentos(@Valid @RequestPart("dados") DenunciaRequest request,
            @RequestPart(value = "arquivos", required = false) org.springframework.web.multipart.MultipartFile[] arquivos,
            org.springframework.security.core.Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarComDocumentos(request, arquivos, autor(auth)));
    }

    @GetMapping("/{id}/documentos/{documentoId}")
    public ResponseEntity<byte[]> baixarDocumento(@PathVariable Long id, @PathVariable java.util.UUID documentoId, org.springframework.security.core.Authentication auth) {
        service.verificarAcesso(id, auth);
        var documento = documentoService.buscar(id, documentoId);
        return ResponseEntity.ok().contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .header("Content-Disposition", org.springframework.http.ContentDisposition.attachment().filename(documento.getNome(), java.nio.charset.StandardCharsets.UTF_8).build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(org.springframework.http.CacheControl.noStore()).body(documento.getConteudo());
    }

    @GetMapping
    public br.jus.tjpb.lyno.litigancia.dto.PaginaDenuncias listar(@RequestParam(required = false) StatusDenuncia status,
            @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "25") int tamanho) {
        return service.listar(status, pagina, tamanho);
    }

    @GetMapping("/{id}")
    public DenunciaResponse buscar(@PathVariable Long id, org.springframework.security.core.Authentication auth) {
        service.verificarAcesso(id, auth);
        return service.buscar(id);
    }

    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<br.jus.tjpb.lyno.litigancia.dto.AcompanhamentoResponse> acompanhar(@PathVariable String protocolo) {
        return ResponseEntity.ok().cacheControl(org.springframework.http.CacheControl.noStore())
                .body(service.acompanhar(protocolo));
    }

    private String autor(org.springframework.security.core.Authentication auth) {
        return auth != null && auth.isAuthenticated()
                && !(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)
                ? auth.getName() : null;
    }

    @PatchMapping("/{id}/status")
    public DenunciaResponse atualizarStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request, org.springframework.security.core.Authentication auth) {
        return service.atualizarStatus(id, request, auth.getName());
    }
}
