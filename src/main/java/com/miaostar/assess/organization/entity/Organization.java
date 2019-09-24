package com.miaostar.assess.organization.entity;

import com.miaostar.assess.organization.OrganizationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 组织机构
 */
@EqualsAndHashCode(callSuper = true, exclude = {"organizations", "positions"})
@ToString(exclude = {"organizations", "positions"})
@Data
@Entity
public class Organization extends AbstractPersistable<Long> {

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String abbr;

    /**
     * 编号
     */
    private String code;

    /**
     * 序号
     */
    private Integer orderNo;

    /**
     * 上级机构
     */
    @ManyToOne
    private Organization up;

    /**
     * 组织机构类型
     */
    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    /**
     * 下级机构
     */
    @OneToMany(mappedBy = "up")
    private Set<Organization> organizations = new LinkedHashSet<>();

    /**
     * 组织机构人员
     */
    @ManyToMany
    private Set<Person> people = new LinkedHashSet<>();

    /**
     * 组织机构岗位
     */
    @OneToMany(mappedBy = "organization")
    private Set<Position> positions = new LinkedHashSet<>();

    /**
     * 描述
     */
    private String remark;
}
