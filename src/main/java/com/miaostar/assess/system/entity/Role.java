package com.miaostar.assess.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 角色
 */
@EqualsAndHashCode(callSuper = true, exclude = {"users"})
@ToString(exclude = {"users"})
@Data
@Entity
@Table(name = "sys_role")
public class Role extends AbstractPersistable<Long> {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编号
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 拥有该角色的用户
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new LinkedHashSet<>();

    /**
     * 该角色可访问的资源
     */
    @ManyToMany
    private Set<Resource> resources = new LinkedHashSet<>();
}
