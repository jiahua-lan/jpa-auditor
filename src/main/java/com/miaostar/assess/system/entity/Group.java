package com.miaostar.assess.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用户组
 */
@EqualsAndHashCode(callSuper = true, exclude = {"users"})
@ToString(exclude = {"users"})
@Data
@Entity
@Table(name = "sys_group")
public class Group extends AbstractPersistable<Long> {

    /**
     * 名称
     */
    @NotEmpty(message = "{Group.name.NotEmpty}")
    private String name;

    /**
     * 编号
     */
    @NotEmpty(message = "{Group.code.NotEmpty}")
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 属于该用户组的用户
     */
    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new LinkedHashSet<>();

    /**
     * 该用户组可访问的资源
     */
    @ManyToMany
    private Set<Resource> resources = new LinkedHashSet<>();

}
