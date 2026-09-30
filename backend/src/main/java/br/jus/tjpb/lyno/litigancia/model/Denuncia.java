package br.jus.tjpb.lyno.litigancia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "denuncias")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long versao;
    @Column(name = "criado_por", length = 120)
    private String criadoPor;

    @Column(name = "numero_protocolo", nullable = false, unique = true, length = 30)
    private String numeroProtocolo;

    // --- Bloco 1: Identificacao da origem ---
    @Column(nullable = false, length = 120)
    private String comarca;

    @Column(name = "nome_comunicante", nullable = false, length = 150)
    private String nomeComunicante;

    @Column(name = "matricula_comunicante", nullable = false, length = 30)
    private String matriculaComunicante;

    // --- Bloco 2: Alvo da suspeita ---
    @Column(name = "oab_advogado", nullable = false, length = 30)
    private String oabAdvogado;

    @Column(name = "cpf_cnpj_parte", length = 30)
    private String cpfCnpjParte;

    // --- Bloco 3: Tipologia ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Tipologia tipologia;

    @Column(name = "tipologia_outros", length = 120)
    private String tipologiaOutros;

    // --- Bloco 4: Materialidade ---
    @ElementCollection
    @CollectionTable(name = "denuncia_processos_paradigmas", joinColumns = @JoinColumn(name = "denuncia_id"))
    @Column(name = "numero_processo", length = 25)
    private List<String> processosParadigmas = new ArrayList<>();

    @Column(name = "relato", length = 500)
    private String relato;

    @ElementCollection
    @CollectionTable(name = "denuncia_anexos", joinColumns = @JoinColumn(name = "denuncia_id"))
    @Column(name = "nome_arquivo", length = 255)
    private List<String> anexos = new ArrayList<>();

    // --- Enriquecimento / Workflow ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private StatusDenuncia status = StatusDenuncia.NOVO;

    @Column(name = "volume_processual_estimado")
    private Integer volumeProcessualEstimado;

    @Column(name = "comarcas_atingidas")
    private Integer comarcasAtingidas;

    @Column(name = "nota_tecnica_resumo", length = 800)
    private String notaTecnicaResumo;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now(java.time.ZoneId.of("America/Recife"));
        if (dataAtualizacao == null) dataAtualizacao = dataCriacao;
    }

    // --- Getters e setters ---

    public Long getId() { return id; }
    public Long getVersao() { return versao; }
    public String getCriadoPor() { return criadoPor; }
    public void setCriadoPor(String criadoPor) { this.criadoPor = criadoPor; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroProtocolo() { return numeroProtocolo; }
    public void setNumeroProtocolo(String numeroProtocolo) { this.numeroProtocolo = numeroProtocolo; }

    public String getComarca() { return comarca; }
    public void setComarca(String comarca) { this.comarca = comarca; }

    public String getNomeComunicante() { return nomeComunicante; }
    public void setNomeComunicante(String nomeComunicante) { this.nomeComunicante = nomeComunicante; }

    public String getMatriculaComunicante() { return matriculaComunicante; }
    public void setMatriculaComunicante(String matriculaComunicante) { this.matriculaComunicante = matriculaComunicante; }

    public String getOabAdvogado() { return oabAdvogado; }
    public void setOabAdvogado(String oabAdvogado) { this.oabAdvogado = oabAdvogado; }

    public String getCpfCnpjParte() { return cpfCnpjParte; }
    public void setCpfCnpjParte(String cpfCnpjParte) { this.cpfCnpjParte = cpfCnpjParte; }

    public Tipologia getTipologia() { return tipologia; }
    public void setTipologia(Tipologia tipologia) { this.tipologia = tipologia; }

    public String getTipologiaOutros() { return tipologiaOutros; }
    public void setTipologiaOutros(String tipologiaOutros) { this.tipologiaOutros = tipologiaOutros; }

    public List<String> getProcessosParadigmas() { return processosParadigmas; }
    public void setProcessosParadigmas(List<String> processosParadigmas) { this.processosParadigmas = processosParadigmas; }

    public String getRelato() { return relato; }
    public void setRelato(String relato) { this.relato = relato; }

    public List<String> getAnexos() { return anexos; }
    public void setAnexos(List<String> anexos) { this.anexos = anexos; }

    public StatusDenuncia getStatus() { return status; }
    public void setStatus(StatusDenuncia status) { this.status = status; }

    public Integer getVolumeProcessualEstimado() { return volumeProcessualEstimado; }
    public void setVolumeProcessualEstimado(Integer volumeProcessualEstimado) { this.volumeProcessualEstimado = volumeProcessualEstimado; }

    public Integer getComarcasAtingidas() { return comarcasAtingidas; }
    public void setComarcasAtingidas(Integer comarcasAtingidas) { this.comarcasAtingidas = comarcasAtingidas; }

    public String getNotaTecnicaResumo() { return notaTecnicaResumo; }
    public void setNotaTecnicaResumo(String notaTecnicaResumo) { this.notaTecnicaResumo = notaTecnicaResumo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
