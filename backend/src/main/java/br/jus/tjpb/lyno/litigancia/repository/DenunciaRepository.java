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

    org.springframework.data.domain.Page<Denuncia> findByStatusOrderByDataCriacaoDescIdDesc(StatusDenuncia status, org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Denuncia> findAllByOrderByDataCriacaoDescIdDesc(org.springframework.data.domain.Pageable pageable);
    long countByStatus(StatusDenuncia status);

    @Query("select count(d) from Denuncia d where d.dataCriacao >= :desde and d.dataCriacao < :ate")
    long countTotal(LocalDateTime desde, LocalDateTime ate);

    @Query("select coalesce(sum(d.volumeProcessualEstimado), 0) from Denuncia d where d.dataCriacao >= :desde and d.dataCriacao < :ate")
    long somaVolumeProcessual(LocalDateTime desde, LocalDateTime ate);

    @Query("select count(distinct d.comarca) from Denuncia d where d.dataCriacao >= :desde and d.dataCriacao < :ate")
    long countComarcasDistintas(LocalDateTime desde, LocalDateTime ate);

    @Query("select d.oabAdvogado as chave, count(d) as total, count(distinct d.comarca) as comarcas " +
           "from Denuncia d where d.dataCriacao >= :desde and d.dataCriacao < :ate " +
           "group by d.oabAdvogado order by count(d) desc, d.oabAdvogado asc")
    List<RankingProjection> rankingReincidencia(LocalDateTime desde, LocalDateTime ate, org.springframework.data.domain.Pageable pageable);

    @Query("select d.cpfCnpjParte as chave, count(d) as total, count(distinct d.comarca) as comarcas " +
           "from Denuncia d where d.dataCriacao >= :desde and d.dataCriacao < :ate and d.cpfCnpjParte is not null and d.cpfCnpjParte <> '' " +
           "group by d.cpfCnpjParte order by count(d) desc, d.cpfCnpjParte asc")
    List<RankingProjection> rankingDocumentos(LocalDateTime desde, LocalDateTime ate, org.springframework.data.domain.Pageable pageable);

    @Query("select d.tipologia as tipologia, count(d) as total from Denuncia d where d.dataCriacao >= :desde and d.dataCriacao < :ate group by d.tipologia order by count(d) desc")
    List<TipologiaProjection> contagemPorTipologia(LocalDateTime desde, LocalDateTime ate);

    @Query("select d.comarca as comarca, count(d) as total from Denuncia d where d.dataCriacao >= :desde and d.dataCriacao < :ate group by d.comarca order by count(d) desc, d.comarca asc")
    List<ComarcaProjection> contagemPorComarca(LocalDateTime desde, LocalDateTime ate);

    @Query("select function('date_trunc', 'week', d.dataCriacao) as semana, count(d) as total " +
           "from Denuncia d where d.dataCriacao >= :desde and d.dataCriacao < :ate group by function('date_trunc', 'week', d.dataCriacao) " +
           "order by semana asc")
    List<TendenciaProjection> tendenciaSemanal(LocalDateTime desde, LocalDateTime ate);

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
