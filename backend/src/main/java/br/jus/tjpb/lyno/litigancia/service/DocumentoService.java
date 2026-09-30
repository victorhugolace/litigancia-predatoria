package br.jus.tjpb.lyno.litigancia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import br.jus.tjpb.lyno.litigancia.model.Documento;
import br.jus.tjpb.lyno.litigancia.repository.DocumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class DocumentoService {
    private final DocumentoRepository repository;
    public DocumentoService(DocumentoRepository repository) { this.repository = repository; }

    public record Arquivo(String nome, byte[] conteudo) {}

    public List<Arquivo> validar(MultipartFile[] arquivos) {
        if (arquivos == null) return List.of();
        if (arquivos.length > 5) throw new IllegalArgumentException("Envie no máximo 5 PDFs");
        var documentos = new ArrayList<Arquivo>();
        for (var arquivo : arquivos) {
            if (arquivo.isEmpty() || arquivo.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Cada PDF deve ter conteúdo e no máximo 10 MB");
            }
            String original = Objects.toString(arquivo.getOriginalFilename(), "").replace('\\', '/');
            String nome = original.substring(original.lastIndexOf('/') + 1).replaceAll("[\\p{Cntrl}]", "").strip();
            if (nome.length() > 255 || !nome.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
                throw new IllegalArgumentException("Envie somente arquivos com extensão PDF e nome de até 255 caracteres");
            }
            try {
                byte[] bytes = arquivo.getBytes();
                String cabecalho = new String(bytes, 0, Math.min(5, bytes.length), StandardCharsets.ISO_8859_1);
                String finalArquivo = new String(bytes, Math.max(0, bytes.length - 1024), Math.min(1024, bytes.length), StandardCharsets.ISO_8859_1);
                if (!cabecalho.equals("%PDF-") || !finalArquivo.contains("%%EOF")) {
                    throw new IllegalArgumentException("O conteúdo de " + nome + " não tem a estrutura básica de um PDF");
                }
                documentos.add(new Arquivo(nome, bytes));
            } catch (IOException ex) { throw new IllegalArgumentException("Não foi possível ler o PDF enviado"); }
        }
        return documentos;
    }

    public void salvar(Long denunciaId, List<Arquivo> arquivos) {
        repository.saveAll(arquivos.stream().map(a -> new Documento(denunciaId, a.nome(), a.conteudo())).toList());
    }

    @Transactional(readOnly = true)
    public Documento buscar(Long denunciaId, UUID documentoId) {
        return repository.findByIdAndDenunciaId(documentoId, denunciaId)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado para esta denúncia"));
    }
}
