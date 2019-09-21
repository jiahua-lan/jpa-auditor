package com.miaostar.auditor.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class User extends AbstractPersistable<Long> {

    private String name;

    private String password;

    @ManyToMany
    private Set<Role> roles = new LinkedHashSet<>();
}
