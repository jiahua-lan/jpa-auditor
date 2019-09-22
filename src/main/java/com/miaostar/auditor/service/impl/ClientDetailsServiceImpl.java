package com.miaostar.auditor.service.impl;

import com.miaostar.auditor.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Primary
@Transactional
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private PasswordEncoder encoder;

    private ClientRepository clientRepository;

    public ClientDetailsServiceImpl(PasswordEncoder encoder, ClientRepository clientRepository) {
        this.encoder = encoder;
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        return clientRepository.findByClientId(clientId).map(client -> {
//            Set<SimpleGrantedAuthority> authorities = client.getAuthorities().stream()
//                    .map(Resource::getCode)
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toSet());

            BaseClientDetails details = new BaseClientDetails();
            details.setClientId(client.getClientId());
            details.setClientSecret(client.getSecret());
            details.setScope(client.getScope());
            details.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
//            details.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
//            details.setAutoApproveScopes(client.getAutoApproveScopes());
//            details.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
//            details.setResourceIds(client.getResourceIds());
//            details.setRegisteredRedirectUri(client.getRegisteredRedirectUris());
//            details.setAuthorities(authorities);
            return details;
        }).orElseThrow(() -> new ClientRegistrationException(clientId));
    }
}

