package com.miaostar.assess.system.handler;

import com.miaostar.assess.system.entity.Group;
import com.miaostar.assess.system.entity.Role;
import com.miaostar.assess.system.entity.User;
import com.miaostar.assess.system.exception.UserNotFoundException;
import com.miaostar.assess.system.repository.GroupRepository;
import com.miaostar.assess.system.repository.RoleRepository;
import com.miaostar.assess.system.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserHandler {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private GroupRepository groupRepository;

    private PasswordEncoder encoder;

    public UserHandler(UserRepository userRepository,
                       RoleRepository roleRepository,
                       GroupRepository groupRepository,
                       PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.encoder = encoder;
    }

    @PreAuthorize("hasAuthority('U0001')")
    @GetMapping(name = "查找用户", value = "/{id}")
    public User find(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @PreAuthorize("hasAuthority('U0002')")
    @PostMapping(name = "创建用户")
    public HttpEntity<?> create(@Valid @RequestBody User user) {
        String encode = encoder.encode(user.getPassword());
        user.setPassword(encode);
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
            u.setUsername(user.getUsername());
            u.setEnable(user.getEnable());
            u.setLocked(user.getLocked());
            return userRepository.save(u);
        }).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('U0005')")
    @PostMapping(name = "分配用户角色", value = "/{id}/roles")
    public HttpEntity<?> assignRoles(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        User entity = userRepository.findById(id).map(user -> {
            List<Role> roles = roleRepository.findAllById(ids);
            user.getRoles().addAll(roles);
            return userRepository.save(user);
        }).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('U0006')")
    @DeleteMapping(name = "移除用户角色", value = "/{id}/roles")
    public HttpEntity<?> removeRoles(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        User entity = userRepository.findById(id).map(user -> {
            List<Role> roles = roleRepository.findAllById(ids);
            user.getRoles().removeAll(roles);
            return userRepository.save(user);
        }).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('U0007')")
    @PostMapping(name = "加入用户组", value = "/{id}/groups")
    public HttpEntity<?> addToGroup(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        User entity = userRepository.findById(id).map(user -> {
            List<Group> groups = groupRepository.findAllById(ids);
            user.getGroups().addAll(groups);
            return userRepository.save(user);
        }).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('U0008')")
    @PostMapping(name = "从用户组中移除", value = "/{id}/groups")
    public HttpEntity<?> removeGroup(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        User entity = userRepository.findById(id).map(user -> {
            List<Group> groups = groupRepository.findAllById(ids);
            user.getGroups().removeAll(groups);
            return userRepository.save(user);
        }).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('U0009')")
    @DeleteMapping(name = "删除用户", value = "/{id}")
    public HttpEntity<?> delete(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
