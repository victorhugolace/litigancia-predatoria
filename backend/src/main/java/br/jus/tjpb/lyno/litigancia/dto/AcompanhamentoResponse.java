package br.jus.tjpb.lyno.litigancia.dto;

import br.jus.tjpb.lyno.litigancia.model.StatusDenuncia;
import java.time.LocalDateTime;

// Projeção pública: detalhes, documentos e notas internas ficam na área gerencial.
public record AcompanhamentoResponse(String numeroProtocolo, StatusDenuncia status,
        String statusDescricao, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {}
