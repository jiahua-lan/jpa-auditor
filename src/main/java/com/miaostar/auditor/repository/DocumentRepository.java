package com.miaostar.auditor.repository;

import com.miaostar.auditor.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
