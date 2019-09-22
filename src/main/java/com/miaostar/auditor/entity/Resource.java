package com.miaostar.auditor.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"roles", "clients"})
@ToString(exclude = {"roles", "clients"})
@Data
@Entity
public class Resource extends AbstractPersistable<Long> {

    private String code;

    private String name;

    private String method;

    private String path;

    private String remark;

    @ManyToMany(mappedBy = "resources")
    private Set<Role> roles = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "authorities")
    private Set<Client> clients = new LinkedHashSet<>();
}
