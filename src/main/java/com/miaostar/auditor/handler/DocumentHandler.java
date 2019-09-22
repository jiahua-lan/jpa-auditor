package com.miaostar.auditor.handler;

import com.miaostar.auditor.entity.Document;
import com.miaostar.auditor.repository.DocumentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentHandler {
    private DocumentRepository documentRepository;

    public DocumentHandler(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @PreAuthorize("hasAuthority('D0001')")
    @PostMapping(name = "创建文档")
    public HttpEntity<?> create(@Valid @RequestBody Document document) {
        Document entity = documentRepository.save(document);
        return ResponseEntity
                .created(URI.create("/document/" + document.getId()))
                .body(entity);
    }

    @PreAuthorize("hasAuthority('D0002')")
    @GetMapping(name = "查找文档", value = "/{id}")
    public HttpEntity<?> find(@PathVariable Long id) {
        Optional<Document> document = documentRepository.findById(id);
        return ResponseEntity.of(document);
    }

    @PreAuthorize("hasAuthority('D0003')")
    @GetMapping(name = "文档列表")
    public HttpEntity<?> findAll(@RequestBody Document document) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Document> example = Example.of(document, matcher);
        List<Document> list = documentRepository.findAll(example);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAuthority('D0004')")
    @PutMapping(name = "修改文档内容", value = "/{id}")
    public HttpEntity<?> replace(@Valid @RequestBody Document document, @PathVariable("id") Long id) {
        Optional<Document> optional = documentRepository.findById(id)
                .map(doc -> {
                    doc.setContent(document.getContent());
                    doc.setTitle(document.getTitle());
                    return documentRepository.save(doc);
                });
        return ResponseEntity.of(optional);
    }

    @PreAuthorize("hasAuthority('D0005')")
    @DeleteMapping(name = "删除文档", value = "/{id}")
    public HttpEntity<?> delete(@PathVariable("id") Long id) {
        documentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
