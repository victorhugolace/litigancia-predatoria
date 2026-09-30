package br.jus.tjpb.lyno.litigancia.repository;

import br.jus.tjpb.lyno.litigancia.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByDenunciaIdOrderByIdDesc(Long denunciaId);
}
