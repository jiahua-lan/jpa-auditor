package com.miaostar.assess.security.service.impl;


import com.miaostar.assess.system.entity.Resource;
import com.miaostar.assess.system.entity.Role;
import com.miaostar.assess.system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service("userService")
@Transactional
public class UserServiceImpl implements UserDetailsService, MessageSourceAware {

    private UserRepository userRepository;

    private MessageSource source;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.source = messageSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> {
                    String[] split = user.getRoles()
                            .stream()
                            .map(Role::getResources)
                            .flatMap(Collection::stream)
                            .map(Resource::getCode)
                            .collect(Collectors.joining(",")).split(",");

                    if (log.isDebugEnabled()) {
                        String message = source.getMessage("User.accessible.resources",
                                new Object[]{Arrays.toString(split)},
                                Locale.getDefault());
                        log.debug(message);
                    }

                    return User.builder()
                            .password(user.getPassword())
                            .username(user.getUsername())
                            .authorities(split)
                            .build();
                }).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
