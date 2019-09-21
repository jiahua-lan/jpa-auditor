package com.miaostar.auditor.service.impl;

import com.miaostar.auditor.entity.Resource;
import com.miaostar.auditor.entity.Role;
import com.miaostar.auditor.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service("userService")
@Transactional
public class UserServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .map(user -> {
                    String[] split = user.getRoles()
                            .stream()
                            .map(Role::getResources)
                            .flatMap(Collection::stream)
                            .map(Resource::getCode)
                            .collect(Collectors.joining(",")).split(",");
                    log.info("{}", Arrays.toString(split));
                    return User.builder()
                            .password(user.getPassword())
                            .username(user.getName())
                            .authorities(split)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
