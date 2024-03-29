package com.miaostar.assess.jpa.aware;


import com.miaostar.assess.system.entity.User;
import com.miaostar.assess.system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@Transactional
public class UserAuditorAware implements AuditorAware<User> {

    private UserRepository userRepository;

    public UserAuditorAware(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .map(Object::toString)
                .map(userRepository::findByUsername)
                .flatMap(Function.identity());
    }
}
