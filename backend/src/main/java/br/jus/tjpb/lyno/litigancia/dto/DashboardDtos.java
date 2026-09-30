package br.jus.tjpb.lyno.litigancia.dto;

import java.time.LocalDate;

public class DashboardDtos {

    public static class KpisResponse {
        public long totalDenuncias;
        public long volumeProcessualAfetado;
        public long comarcasAfetadas;

        public KpisResponse(long totalDenuncias, long volumeProcessualAfetado, long comarcasAfetadas) {
            this.totalDenuncias = totalDenuncias;
            this.volumeProcessualAfetado = volumeProcessualAfetado;
            this.comarcasAfetadas = comarcasAfetadas;
        }
    }

    public static class RankingItem {
        public String identificador;
        public long totalDenuncias;
        public long comarcasEnvolvidas;

        public RankingItem(String oabAdvogado, long totalDenuncias, long comarcasEnvolvidas) {
            this.identificador = oabAdvogado;
            this.totalDenuncias = totalDenuncias;
            this.comarcasEnvolvidas = comarcasEnvolvidas;
        }
    }

    public static class TipologiaItem {
        public String tipologia;
        public String descricao;
        public long total;
        public double percentual;

        public TipologiaItem(String tipologia, String descricao, long total, double percentual) {
            this.tipologia = tipologia;
            this.descricao = descricao;
            this.total = total;
            this.percentual = percentual;
        }
    }

    public static class ComarcaItem {
        public String comarca;
        public long total;

        public ComarcaItem(String comarca, long total) {
            this.comarca = comarca;
            this.total = total;
        }
    }

    public static class TendenciaItem {
        public LocalDate semana;
        public long total;

        public TendenciaItem(LocalDate semana, long total) {
            this.semana = semana;
            this.total = total;
        }
    }

    public static class StatusUpdateRequest {
        @jakarta.validation.constraints.NotNull
        @jakarta.validation.constraints.Min(0)
        public Long versao;
        public br.jus.tjpb.lyno.litigancia.model.StatusDenuncia status;
        public boolean limparVolume;
        public boolean limparComarcas;
        @jakarta.validation.constraints.Min(0)
        public Integer volumeProcessualEstimado;
        @jakarta.validation.constraints.Min(1)
        public Integer comarcasAtingidas;
        @jakarta.validation.constraints.Size(max = 800)
        public String notaTecnicaResumo;
    }
}
