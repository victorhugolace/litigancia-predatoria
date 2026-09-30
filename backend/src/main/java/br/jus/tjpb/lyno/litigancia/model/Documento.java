package br.jus.tjpb.lyno.litigancia.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "denuncia_documentos")
public class Documento {
    @Id private UUID id;
    @Column(name = "denuncia_id", nullable = false) private Long denunciaId;
    @Column(nullable = false, length = 255) private String nome;
    @Column(nullable = false) private long tamanho;
    @Column(nullable = false, columnDefinition = "bytea") private byte[] conteudo;

    protected Documento() {}
    public Documento(Long denunciaId, String nome, byte[] conteudo) {
        this.id = UUID.randomUUID(); this.denunciaId = denunciaId; this.nome = nome;
        this.tamanho = conteudo.length; this.conteudo = conteudo;
    }
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public byte[] getConteudo() { return conteudo; }
}
