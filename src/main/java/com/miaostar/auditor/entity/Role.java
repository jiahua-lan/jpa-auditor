package com.miaostar.auditor.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true,exclude = {"users"})
@ToString(exclude = {"users"})
@Data
@Entity
public class Role extends AbstractPersistable<Long> {

    private String name;

    private String code;

    @ManyToMany
    private Set<Resource> resources = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new LinkedHashSet<>();

}
