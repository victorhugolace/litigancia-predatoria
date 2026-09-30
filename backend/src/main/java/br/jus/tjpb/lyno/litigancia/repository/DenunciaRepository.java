package br.jus.tjpb.lyno.litigancia.repository;

import br.jus.tjpb.lyno.litigancia.model.Denuncia;
import br.jus.tjpb.lyno.litigancia.model.StatusDenuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    @Query(value = "select nextval('denuncia_protocolo_seq')", nativeQuery = true)
    long proximoProtocolo();

    List<Denuncia> findByStatusOrderByDataCriacaoDesc(StatusDenuncia status);

    List<Denuncia> findAllByOrderByDataCriacaoDesc();

    @Query("select count(d) from Denuncia d")
    long countTotal();

    @Query("select coalesce(sum(d.volumeProcessualEstimado), 0) from Denuncia d")
    long somaVolumeProcessual();

    @Query("select count(distinct d.comarca) from Denuncia d")
    long countComarcasDistintas();

    @Query("select d.oabAdvogado as chave, count(d) as total, count(distinct d.comarca) as comarcas " +
           "from Denuncia d group by d.oabAdvogado order by count(d) desc")
    List<RankingProjection> rankingReincidencia();

    @Query("select d.tipologia as tipologia, count(d) as total from Denuncia d group by d.tipologia order by count(d) desc")
    List<TipologiaProjection> contagemPorTipologia();

    @Query("select d.comarca as comarca, count(d) as total from Denuncia d group by d.comarca order by count(d) desc")
    List<ComarcaProjection> contagemPorComarca();

    @Query("select function('date_trunc', 'week', d.dataCriacao) as semana, count(d) as total " +
           "from Denuncia d where d.dataCriacao >= :desde group by function('date_trunc', 'week', d.dataCriacao) " +
           "order by semana asc")
    List<TendenciaProjection> tendenciaSemanal(LocalDateTime desde);

    interface RankingProjection {
        String getChave();
        Long getTotal();
        Long getComarcas();
    }

    interface TipologiaProjection {
        br.jus.tjpb.lyno.litigancia.model.Tipologia getTipologia();
        Long getTotal();
    }

    interface ComarcaProjection {
        String getComarca();
        Long getTotal();
    }

    interface TendenciaProjection {
        LocalDateTime getSemana();
        Long getTotal();
    }
}
