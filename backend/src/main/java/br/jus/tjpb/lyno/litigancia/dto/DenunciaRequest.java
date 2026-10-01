package br.jus.tjpb.lyno.litigancia.dto;

import br.jus.tjpb.lyno.litigancia.model.Tipologia;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Locale;
import java.util.HashSet;

public class DenunciaRequest {

    @NotBlank(message = "Informe a comarca/unidade judiciária")
    @Size(max = 120)
    private String comarca;

    @NotBlank(message = "Informe o nome do comunicante")
    @Size(max = 150)
    private String nomeComunicante;

    @Size(max = 30)
    private String matriculaComunicante;

    @NotBlank(message = "Informe o número da OAB do advogado suspeito")
    @Pattern(regexp = "[A-Z]{2}[0-9]{1,10}[A-Z]?", message = "Informe a UF e o número da OAB (ex.: PB12345)")
    private String oabAdvogado;

    @Pattern(regexp = "([0-9]{11}|[A-Z0-9]{12}[0-9]{2})?", message = "Informe um CPF ou CNPJ completo")
    private String cpfCnpjParte;

    @NotNull(message = "Selecione a tipologia da ação")
    private Tipologia tipologia;

    @Size(max = 120, message = "Descreva a tipologia em até 120 caracteres")
    private String tipologiaOutros;

    @NotNull
    @Size(min = 3, max = 5, message = "Informe entre 3 e 5 processos paradigmas")
    private List<@NotBlank @Pattern(regexp = "\\d{7}-\\d{2}\\.\\d{4}\\.\\d\\.\\d{2}\\.\\d{4}",
            message = "Número de processo deve seguir a máscara CNJ NNNNNNN-DD.AAAA.J.TR.OOOO") String> processosParadigmas;

    @NotBlank(message = "Descreva brevemente o padrão observado")
    @Size(max = 500, message = "O relato deve ter no máximo 500 caracteres")
    private String relato;

    @Size(max = 0, message = "Envie os arquivos PDF pelo formulário multipart, não apenas seus nomes")
    private List<String> anexos;

    @AssertTrue(message = "Informe processos paradigmas distintos")
    public boolean isProcessosDistintos() {
        return processosParadigmas == null || new HashSet<>(processosParadigmas).size() == processosParadigmas.size();
    }

    @AssertTrue(message = "Descreva a tipologia quando selecionar Outros")
    public boolean isDescricaoOutrosValida() {
        return tipologia != Tipologia.OUTROS || (tipologiaOutros != null && !tipologiaOutros.isBlank());
    }

    @AssertTrue(message = "CPF/CNPJ com dígitos verificadores inválidos")
    public boolean isDocumentoValido() {
        if (cpfCnpjParte == null || cpfCnpjParte.isEmpty()) return true;
        if (!cpfCnpjParte.matches("[0-9]{11}|[A-Z0-9]{12}[0-9]{2}")) return false;
        if (cpfCnpjParte.chars().distinct().count() == 1) return false;
        int length = cpfCnpjParte.length();
        for (int i = length - 2; i < length; i++) {
            int sum = 0;
            for (int j = 0; j < i; j++) {
                int weight = length == 11 ? i + 1 - j : (i - 1 - j) % 8 + 2;
                sum += (cpfCnpjParte.charAt(j) - '0') * weight;
            }
            int remainder = sum % 11;
            if (cpfCnpjParte.charAt(i) - '0' != (remainder < 2 ? 0 : 11 - remainder)) return false;
        }
        return true;
    }

    private static String trim(String value) { return value == null ? null : value.strip(); }

    // Getters e setters

    public String getComarca() { return comarca; }
    public void setComarca(String comarca) { this.comarca = trim(comarca); }

    public String getNomeComunicante() { return nomeComunicante; }
    public void setNomeComunicante(String nomeComunicante) { this.nomeComunicante = trim(nomeComunicante); }

    public String getMatriculaComunicante() { return matriculaComunicante; }
    public void setMatriculaComunicante(String matriculaComunicante) { this.matriculaComunicante = trim(matriculaComunicante); }

    public String getOabAdvogado() { return oabAdvogado; }
    public void setOabAdvogado(String oabAdvogado) { this.oabAdvogado = oabAdvogado == null ? null : oabAdvogado.toUpperCase(Locale.ROOT).replaceAll("[\\s/.-]", ""); }

    public String getCpfCnpjParte() { return cpfCnpjParte; }
    public void setCpfCnpjParte(String cpfCnpjParte) { this.cpfCnpjParte = cpfCnpjParte == null ? null : cpfCnpjParte.toUpperCase(Locale.ROOT).replaceAll("[\\s./-]", ""); }

    public Tipologia getTipologia() { return tipologia; }
    public void setTipologia(Tipologia tipologia) { this.tipologia = tipologia; }

    public String getTipologiaOutros() { return tipologiaOutros; }
    public void setTipologiaOutros(String tipologiaOutros) { this.tipologiaOutros = trim(tipologiaOutros); }

    public List<String> getProcessosParadigmas() { return processosParadigmas; }
    public void setProcessosParadigmas(List<String> processosParadigmas) { this.processosParadigmas = processosParadigmas; }

    public String getRelato() { return relato; }
    public void setRelato(String relato) { this.relato = trim(relato); }

    public List<String> getAnexos() { return anexos; }
    public void setAnexos(List<String> anexos) { this.anexos = anexos; }
}
