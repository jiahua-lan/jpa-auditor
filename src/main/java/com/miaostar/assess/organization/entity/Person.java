package com.miaostar.assess.organization.entity;

import com.miaostar.assess.system.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"organizations", "positions", "users"})
@ToString(exclude = {"organizations", "positions", "users"})
@Data
@Entity
public class Person extends AbstractPersistable<Long> {

    /**
     * 人员编号
     */
    private String code;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 所属组织机构
     */
    @ManyToMany(mappedBy = "people")
    private Set<Organization> organizations = new LinkedHashSet<>();

    /**
     * 担任岗位
     */
    @ManyToMany(mappedBy = "people")
    private Set<Position> positions = new LinkedHashSet<>();

    /**
     * 帐号
     */
    @OneToMany(mappedBy = "person")
    private Set<User> users = new LinkedHashSet<>();
}
