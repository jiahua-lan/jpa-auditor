package com.miaostar.assess.system.handler;

import com.miaostar.assess.system.entity.Resource;
import com.miaostar.assess.system.entity.Role;
import com.miaostar.assess.system.exception.RoleNotFoundException;
import com.miaostar.assess.system.repository.ResourceRepository;
import com.miaostar.assess.system.repository.RoleRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleHandler {

    private RoleRepository roleRepository;

    private ResourceRepository resourceRepository;

    public RoleHandler(RoleRepository roleRepository,
                       ResourceRepository resourceRepository) {
        this.roleRepository = roleRepository;
        this.resourceRepository = resourceRepository;
    }

    @PreAuthorize("hasAuthority('R0001')")
    @GetMapping(name = "查找角色", value = "/{id}")
    public HttpEntity<?> find(@PathVariable("id") Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(RoleNotFoundException::new);
        return ResponseEntity.ok(role);
    }

    @PreAuthorize("hasAuthority('R0002')")
    @GetMapping(name = "角色列表")
    public HttpEntity<?> findAll(@RequestBody Role role) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Role> example = Example.of(role, matcher);
        List<Role> roles = roleRepository.findAll(example);
        return ResponseEntity.ok(roles);
    }

    @PreAuthorize("hasAuthority('R0003')")
    @PostMapping(name = "创建角色")
    public HttpEntity<?> create(@Valid @RequestBody Role role) {
        Role entity = roleRepository.save(role);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('R0004')")
    @PutMapping(name = "修改角色信息", value = "/{id}")
    public HttpEntity<?> replace(@Valid @RequestBody Role role, @PathVariable("id") Long id) {
        Role entity = roleRepository.findById(id).map(ro -> {
            ro.setName(role.getName());
            ro.setCode(role.getCode());
            ro.setRemark(role.getRemark());
            return roleRepository.save(ro);
        }).orElseThrow(RoleNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('R0005')")
    @DeleteMapping(name = "删除角色", value = "/{id}")
    public HttpEntity<?> delete(@PathVariable("id") Long id) {
        roleRepository.findById(id).ifPresent(role -> {
            role.getUsers().forEach(user -> user.getRoles().remove(role));
            role.getResources().forEach(resource -> resource.getRoles().remove(role));
            roleRepository.deleteById(id);
        });
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('R0006')")
    @GetMapping(name = "角色资源列表", value = "/{id}/resources")
    public HttpEntity<?> listResources(@PathVariable("id") Long id) {
        Set<Resource> resources = roleRepository.findById(id)
                .map(Role::getResources)
                .orElseThrow(RoleNotFoundException::new);
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("hasAuthority('R0007')")
    @PostMapping(name = "分配角色资源", value = "/{id}/resources")
    public HttpEntity<?> allocation(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        Role entity = roleRepository.findById(id).map(role -> {
            List<Resource> list = resourceRepository.findAllById(ids);
            role.getResources().addAll(list);
            return roleRepository.save(role);
        }).orElseThrow(RoleNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('R0008')")
    @DeleteMapping(name = "移除角色资源", value = "/{id}/resources")
    public HttpEntity<?> remove(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        Role entity = roleRepository.findById(id).map(role -> {
            List<Resource> list = resourceRepository.findAllById(ids);
            role.getResources().removeAll(list);
            return roleRepository.save(role);
        }).orElseThrow(RoleNotFoundException::new);
        return ResponseEntity.ok(entity);
    }
}
