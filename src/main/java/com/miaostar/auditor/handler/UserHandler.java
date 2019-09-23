package com.miaostar.auditor.handler;

import com.miaostar.auditor.entity.User;
import com.miaostar.auditor.exception.UserNotFoundException;
import com.miaostar.auditor.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserHandler {

    private UserRepository userRepository;

    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAuthority('U0001')")
    @GetMapping(name = "查找用户", value = "/{id}")
    public User find(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @PreAuthorize("hasAuthority('U0002')")
    @PostMapping(name = "创建用户")
    public HttpEntity<?> create(@Valid @RequestBody User user) {
        User entity = userRepository.save(user);
        return ResponseEntity
                .created(URI.create("/users/" + entity.getId()))
                .body(entity);
    }

    @PreAuthorize("hasAuthority('U0003')")
    @GetMapping(name = "用户列表")
    public HttpEntity<?> findAll(@RequestBody User user) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<User> example = Example.of(user, matcher);
        List<User> list = userRepository.findAll(example);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAuthority('U0004')")
    @PutMapping(name = "修改用户信息", value = "/{id}")
    public HttpEntity<?> replace(@Valid @RequestBody User user, @PathVariable("id") Long id) {
        User entity = userRepository.findById(id).map(u -> {
            u.setName(user.getName());
            return userRepository.save(u);
        }).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(entity);
    }
}
