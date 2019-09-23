package com.miaostar.auditor.service.impl;

import com.miaostar.auditor.entity.Resource;
import com.miaostar.auditor.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Primary
@Transactional
public class ClientDetailsServiceImpl implements ClientDetailsService, MessageSourceAware {

    private PasswordEncoder encoder;

    private ClientRepository clientRepository;

    private MessageSource source;

    public ClientDetailsServiceImpl(PasswordEncoder encoder, ClientRepository clientRepository) {
        this.encoder = encoder;
        this.clientRepository = clientRepository;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.source = messageSource;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        return clientRepository.findByClientId(clientId).map(client -> {
            Supplier<Stream<Resource>> supplier = () -> client.getAuthorities().stream();

            Set<GrantedAuthority> authorities = supplier.get()
                    .map(Resource::getCode)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            if (log.isDebugEnabled()) {
                String collect = supplier.get()
                        .sorted(Comparator.comparing(Resource::getCode))
                        .map(resource -> source.getMessage("Resource.Info", new Object[]{resource.getCode(), resource.getName()}, Locale.getDefault()))
                        .collect(Collectors.joining(","));
                String message = source.getMessage("Client.access.resources", new Object[]{collect}, Locale.getDefault());
                log.debug(message);
            }

            BaseClientDetails details = new BaseClientDetails();
            details.setClientId(client.getClientId());
            details.setClientSecret(client.getSecret());
            details.setScope(client.getScope());
            details.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
            details.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
            details.setAutoApproveScopes(client.getAutoApproveScopes());
            details.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
            details.setAuthorities(authorities);
            details.setResourceIds(client.getResourceIds());
            details.setRegisteredRedirectUri(client.getRegisteredRedirectUris());
            return details;
        }).orElseThrow(() -> new ClientRegistrationException(clientId));
    }
}

