package com.miaostar.auditor.handler;

import com.miaostar.auditor.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserHandler {

    private UserRepository userRepository;

    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAuthority('U0001')")
    @GetMapping(name = "查找用户",value = "/users/{id}")
    public HttpEntity<?> find(@PathVariable("id") Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }
}
