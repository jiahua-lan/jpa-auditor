package com.miaostar.assess.security.service.impl;


import com.miaostar.assess.security.domain.ClientDetailsImpl;
import com.miaostar.assess.system.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Primary
@Transactional
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private ClientRepository clientRepository;

    public ClientDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        return clientRepository.findByClientId(clientId)
                .map(ClientDetailsImpl::new)
                .orElseThrow(() -> new ClientRegistrationException(clientId));
    }
}

