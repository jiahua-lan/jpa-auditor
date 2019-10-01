package com.miaostar.assess.security.domain;

import com.miaostar.assess.system.entity.Client;
import com.miaostar.assess.system.entity.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;
import java.util.stream.Collectors;

public class ClientDetailsImpl implements ClientDetails {

    private Client client;

    private Map<String, Object> additionalInformation = Collections.emptyMap();

    public ClientDetailsImpl(Client client) {
        this.client = client;
    }

    @Override
    public String getClientId() {
        return client.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        return client.getResourceIds();
    }

    @Override
    public boolean isSecretRequired() {
        return Objects.nonNull(client.getClientSecret());
    }

    @Override
    public String getClientSecret() {
        return client.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return Objects.nonNull(client.getScope()) && !client.getScope().isEmpty();
    }

    @Override
    public Set<String> getScope() {
        return client.getScope();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return client.getAuthorizedGrantTypes();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return client.getRegisteredRedirectUris();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return client.getAuthorities().stream()
                .map(Resource::getCode)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return client.getAccessTokenValiditySeconds();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return client.getRefreshTokenValiditySeconds();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return client.getScope().stream().anyMatch(s -> Objects.equals(scope, s) || s.matches(scope));
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
