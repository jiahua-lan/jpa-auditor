package com.miaostar.auditor.repository;

import com.miaostar.auditor.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
