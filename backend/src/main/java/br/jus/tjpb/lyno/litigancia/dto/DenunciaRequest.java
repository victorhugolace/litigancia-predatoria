package br.jus.tjpb.lyno.litigancia.dto;

import br.jus.tjpb.lyno.litigancia.model.Tipologia;
import jakarta.validation.constraints.*;

import java.util.List;

public class DenunciaRequest {

    @NotBlank(message = "Informe a comarca/unidade judiciária")
    private String comarca;

    @NotBlank(message = "Informe o nome do comunicante")
    private String nomeComunicante;

    @NotBlank(message = "Informe a matrícula do comunicante")
    private String matriculaComunicante;

    @NotBlank(message = "Informe o número da OAB do advogado suspeito")
    private String oabAdvogado;

    private String cpfCnpjParte;

    @NotNull(message = "Selecione a tipologia da ação")
    private Tipologia tipologia;

    @Size(max = 120, message = "Descreva a tipologia em até 120 caracteres")
    private String tipologiaOutros;

    @NotNull
    @Size(min = 3, max = 5, message = "Informe entre 3 e 5 processos paradigmas")
    private List<@Pattern(regexp = "\\d{7}-\\d{2}\\.\\d{4}\\.\\d\\.\\d{2}\\.\\d{4}",
            message = "Número de processo deve seguir a máscara CNJ NNNNNNN-DD.AAAA.J.TR.OOOO") String> processosParadigmas;

    @NotBlank(message = "Descreva brevemente o padrão observado")
    @Size(max = 500, message = "O relato deve ter no máximo 500 caracteres")
    private String relato;

    private List<String> anexos;

    // Getters e setters

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
}
