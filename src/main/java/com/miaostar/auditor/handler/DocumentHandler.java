package com.miaostar.auditor.handler;

import com.miaostar.auditor.entity.Document;
import com.miaostar.auditor.repository.DocumentRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
public class DocumentHandler {
    private DocumentRepository documentRepository;

    public DocumentHandler(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @PreAuthorize("hasAuthority('D0001')")
    @PostMapping(name = "创建文档", value = "/documents")
    public HttpEntity<?> create(@RequestBody Document document) {
        Document entity = documentRepository.save(document);
        return ResponseEntity
                .created(URI.create("/document/" + document.getId()))
                .body(entity);
    }

    @PreAuthorize("hasAuthority('D0002')")
    @GetMapping(name = "查找文档", value = "/documents/{id}")
    public HttpEntity<?> find(@PathVariable Long id) {
        Optional<Document> document = documentRepository.findById(id);
        return ResponseEntity.of(document);
    }

}
