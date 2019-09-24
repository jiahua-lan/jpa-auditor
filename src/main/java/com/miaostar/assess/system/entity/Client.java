package com.miaostar.assess.system.entity;

import com.miaostar.assess.jpa.converter.StringToSetConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Client extends AbstractPersistable<Long> {

    @NotEmpty(message = "{client.clientId.NotEmpty}")
    private String clientId;

    @NotEmpty(message = "{client.secret.NotEmpty}")
    private String clientSecret;

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

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    @ManyToMany
    private Set<Resource> authorities = new LinkedHashSet<>();
}
