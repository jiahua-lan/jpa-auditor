package com.miaostar.auditor.entity;

import com.miaostar.auditor.converter.StringToSetConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Client extends AbstractAuditable<User, Long> {

    private String clientId;

    private String secret;

    @Convert(converter = StringToSetConverter.class)
    private Set<String> scope = new LinkedHashSet<>();

    @Convert(converter = StringToSetConverter.class)
    private Set<String> resourceIds = new LinkedHashSet<>();

    @Convert(converter = StringToSetConverter.class)
    private Set<String> authorizedGrantTypes = new LinkedHashSet<>();

    @Convert(converter = StringToSetConverter.class)
    private Set<String> registeredRedirectUris = new LinkedHashSet<>();

    @Convert(converter = StringToSetConverter.class)
    private Set<String> autoApproveScopes = new LinkedHashSet<>();

    @ManyToMany
    private Set<Resource> authorities = new LinkedHashSet<>();

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

}
