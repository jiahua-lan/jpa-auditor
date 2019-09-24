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
 * 系统资源
 */
@EqualsAndHashCode(callSuper = true, exclude = {"roles", "groups", "clients"})
@ToString(exclude = {"roles", "groups", "clients"})
@Data
@Entity
@Table(name = "sys_resource")
public class Resource extends AbstractPersistable<Long> {

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源编号
     */
    private String code;

    /**
     * 资源地址表达式
     */
    private String pattern;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 备注
     */
    private String remark;

    /**
     * 拥有访问权限的角色
     */
    @ManyToMany(mappedBy = "resources")
    private Set<Role> roles = new LinkedHashSet<>();

    /**
     * 拥有访问权限的用户组
     */
    @ManyToMany(mappedBy = "resources")
    private Set<Group> groups = new LinkedHashSet<>();

    /**
     * 拥有访问权限的客户端
     */
    @ManyToMany(mappedBy = "authorities")
    private Set<Client> clients = new LinkedHashSet<>();
}
