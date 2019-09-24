package com.miaostar.assess.organization.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Position extends AbstractPersistable<Long> {

    private String name;

    private String code;

    @ManyToMany
    private Set<Person> people = new LinkedHashSet<>();

    @ManyToOne
    private Organization organization;
}
