package com.miaostar.assess.organization.repository;

import com.miaostar.assess.organization.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
