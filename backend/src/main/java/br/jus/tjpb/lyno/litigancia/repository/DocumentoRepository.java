package br.jus.tjpb.lyno.litigancia.repository;

import br.jus.tjpb.lyno.litigancia.model.Documento;
import br.jus.tjpb.lyno.litigancia.dto.DocumentoInfo;
import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface DocumentoRepository extends JpaRepository<Documento, UUID> {
    @Query("select new br.jus.tjpb.lyno.litigancia.dto.DocumentoInfo(d.id, d.nome, d.tamanho) from Documento d where d.denunciaId = :denunciaId order by d.nome, d.id")
    List<DocumentoInfo> listarMetadados(Long denunciaId);
    Optional<Documento> findByIdAndDenunciaId(UUID id, Long denunciaId);
}
