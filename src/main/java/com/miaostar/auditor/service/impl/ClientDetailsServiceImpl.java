package com.miaostar.auditor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@Primary
@Transactional
public class ClientDetailsServiceImpl implements ClientDetailsService {
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails details = new BaseClientDetails();
        details.setClientId("client");
        details.setScope(Collections.singleton("all"));
        details.setClientSecret("$2a$10$mU84ca7/hZp0uEPNKZskCeDjy3e8IziCk9LHvne3egPaHuyuGQ1Cm");
        details.setAuthorizedGrantTypes(Collections.singleton("password"));
        return details;
    }
}

