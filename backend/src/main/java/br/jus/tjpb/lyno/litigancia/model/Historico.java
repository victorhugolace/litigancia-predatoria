package br.jus.tjpb.lyno.litigancia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "denuncia_historico")
public class Historico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "denuncia_id", nullable = false) private Long denunciaId;
    @Enumerated(EnumType.STRING) @Column(name = "status_anterior", length = 40) private StatusDenuncia statusAnterior;
    @Enumerated(EnumType.STRING) @Column(name = "status_novo", nullable = false, length = 40) private StatusDenuncia statusNovo;
    @Column(nullable = false, length = 120) private String autor;
    @Column(nullable = false) private LocalDateTime data;
    private Integer volume;
    private Integer comarcas;
    @Column(length = 800) private String nota;

    protected Historico() {}
    public Historico(Denuncia d, StatusDenuncia anterior, String autor, LocalDateTime data) {
        this.denunciaId = d.getId(); this.statusAnterior = anterior; this.statusNovo = d.getStatus();
        this.autor = autor; this.data = data; this.volume = d.getVolumeProcessualEstimado();
        this.comarcas = d.getComarcasAtingidas(); this.nota = d.getNotaTecnicaResumo();
    }
    public Long getId() { return id; }
    public StatusDenuncia getStatusAnterior() { return statusAnterior; }
    public StatusDenuncia getStatusNovo() { return statusNovo; }
    public String getAutor() { return autor; }
    public LocalDateTime getData() { return data; }
    public Integer getVolume() { return volume; }
    public Integer getComarcas() { return comarcas; }
    public String getNota() { return nota; }
}
