package com.miaostar.assess.organization.repository;

import com.miaostar.assess.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
