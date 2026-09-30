package br.jus.tjpb.lyno.litigancia.dto;

import br.jus.tjpb.lyno.litigancia.model.Denuncia;
import br.jus.tjpb.lyno.litigancia.model.StatusDenuncia;
import br.jus.tjpb.lyno.litigancia.model.Tipologia;

import java.time.LocalDateTime;
import java.util.List;

public class DenunciaResponse {
    private Long id;
    private String numeroProtocolo;
    private String comarca;
    private String nomeComunicante;
    private String matriculaComunicante;
    private String oabAdvogado;
    private String cpfCnpjParte;
    private Tipologia tipologia;
    private String tipologiaDescricao;
    private String tipologiaOutros;
    private List<String> processosParadigmas;
    private String relato;
    private List<String> anexos;
    private StatusDenuncia status;
    private String statusDescricao;
    private Integer volumeProcessualEstimado;
    private Integer comarcasAtingidas;
    private String notaTecnicaResumo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public static DenunciaResponse from(Denuncia d) {
        DenunciaResponse r = new DenunciaResponse();
        r.id = d.getId();
        r.numeroProtocolo = d.getNumeroProtocolo();
        r.comarca = d.getComarca();
        r.nomeComunicante = d.getNomeComunicante();
        r.matriculaComunicante = d.getMatriculaComunicante();
        r.oabAdvogado = d.getOabAdvogado();
        r.cpfCnpjParte = d.getCpfCnpjParte();
        r.tipologia = d.getTipologia();
        r.tipologiaDescricao = d.getTipologia() != null ? d.getTipologia().getDescricao() : null;
        r.tipologiaOutros = d.getTipologiaOutros();
        r.processosParadigmas = List.copyOf(d.getProcessosParadigmas());
        r.relato = d.getRelato();
        r.anexos = List.copyOf(d.getAnexos());
        r.status = d.getStatus();
        r.statusDescricao = d.getStatus() != null ? d.getStatus().getDescricao() : null;
        r.volumeProcessualEstimado = d.getVolumeProcessualEstimado();
        r.comarcasAtingidas = d.getComarcasAtingidas();
        r.notaTecnicaResumo = d.getNotaTecnicaResumo();
        r.dataCriacao = d.getDataCriacao();
        r.dataAtualizacao = d.getDataAtualizacao();
        return r;
    }

    // Getters (no setters needed for a read-only projection, but Jackson needs getters)

    public Long getId() { return id; }
    public String getNumeroProtocolo() { return numeroProtocolo; }
    public String getComarca() { return comarca; }
    public String getNomeComunicante() { return nomeComunicante; }
    public String getMatriculaComunicante() { return matriculaComunicante; }
    public String getOabAdvogado() { return oabAdvogado; }
    public String getCpfCnpjParte() { return cpfCnpjParte; }
    public Tipologia getTipologia() { return tipologia; }
    public String getTipologiaDescricao() { return tipologiaDescricao; }
    public String getTipologiaOutros() { return tipologiaOutros; }
    public List<String> getProcessosParadigmas() { return processosParadigmas; }
    public String getRelato() { return relato; }
    public List<String> getAnexos() { return anexos; }
    public StatusDenuncia getStatus() { return status; }
    public String getStatusDescricao() { return statusDescricao; }
    public Integer getVolumeProcessualEstimado() { return volumeProcessualEstimado; }
    public Integer getComarcasAtingidas() { return comarcasAtingidas; }
    public String getNotaTecnicaResumo() { return notaTecnicaResumo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
}
