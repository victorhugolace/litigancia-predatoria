package br.jus.tjpb.lyno.litigancia.dto;

import java.util.List;
import java.util.Map;
import br.jus.tjpb.lyno.litigancia.model.StatusDenuncia;

public record PaginaDenuncias(List<DenunciaResponse> itens, long total, int pagina, int totalPaginas,
                             Map<StatusDenuncia, Long> totaisPorStatus) {}
