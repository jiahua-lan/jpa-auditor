package com.miaostar.assess.system.entity;

import com.miaostar.assess.organization.entity.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user")
public class User extends AbstractPersistable<Long> {

    /**
     * 用户名
     */
    @NotEmpty(message = "{User.username.NotEmpty}")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "{User.password.NotEmpty}")
    private String password;

    /**
     * 是否可用
     * <p>
     * true:可用
     * false:不可用
     */
    private Boolean enable;

    /**
     * 是否被锁定
     * <p>
     * true:是
     * false:否
     */
    private Boolean locked;

    /**
     * 用户所有者
     */
    @ManyToOne
    private Person person;

    /**
     * 用户拥有的角色
     */
    @ManyToMany
    private Set<Role> roles = new LinkedHashSet<>();

    /**
     * 用户所属的用户组
     */
    @ManyToMany
    private Set<Group> groups = new LinkedHashSet<>();
}
