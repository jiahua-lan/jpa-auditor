package com.miaostar.assess.system.handler;

import com.miaostar.assess.system.entity.Group;
import com.miaostar.assess.system.entity.Resource;
import com.miaostar.assess.system.exception.GroupNotFoundException;
import com.miaostar.assess.system.repository.GroupRepository;
import com.miaostar.assess.system.repository.ResourceRepository;
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
@RequestMapping("/groups")
public class GroupHandler {

    private GroupRepository groupRepository;

    private ResourceRepository resourceRepository;

    public GroupHandler(GroupRepository groupRepository,
                        ResourceRepository resourceRepository) {
        this.groupRepository = groupRepository;
        this.resourceRepository = resourceRepository;
    }

    @PreAuthorize("hasAuthority('G0001')")
    @GetMapping(name = "查找用户组", value = "/{id}")
    public HttpEntity<?> find(@PathVariable("id") Long id) {
        Group group = groupRepository.findById(id).orElseThrow(GroupNotFoundException::new);
        return ResponseEntity.ok(group);
    }

    @PreAuthorize("hasAuthority('G0002')")
    @GetMapping(name = "用户组列表")
    public HttpEntity<?> findAll(@RequestBody Group group) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Group> example = Example.of(group, matcher);
        List<Group> list = groupRepository.findAll(example);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAuthority('G0003')")
    @PostMapping(name = "创建用户组")
    public HttpEntity<?> create(@Valid @RequestBody Group group) {
        Group entity = groupRepository.save(group);
        return ResponseEntity.created(URI.create("/groups/" + entity.getId()))
                .body(entity);
    }

    @PreAuthorize("hasAuthority('G0004')")
    @PutMapping(name = "修改用户组", value = "/{id}")
    public HttpEntity<?> replace(@Valid @RequestBody Group group, @PathVariable("id") Long id) {
        Group entity = groupRepository.findById(id).map(g -> {
            g.setName(group.getName());
            g.setCode(group.getCode());
            g.setRemark(group.getRemark());
            return groupRepository.save(g);
        }).orElseThrow(GroupNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('G0005')")
    @DeleteMapping(name = "删除用户组", value = "/{id}")
    public HttpEntity<?> delete(@PathVariable("id") Long id) {
        groupRepository.findById(id).ifPresent(group -> {
            group.getResources().forEach(resource -> resource.getGroups().remove(group));
            group.getUsers().forEach(user -> user.getGroups().remove(group));
            groupRepository.deleteById(id);
        });
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('G0006')")
    @PostMapping(name = "分配资源", value = "/{id}/resources")
    public HttpEntity<?> allocation(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        Group entity = groupRepository.findById(id).map(group -> {
            List<Resource> resources = resourceRepository.findAllById(ids);
            group.getResources().addAll(resources);
            return groupRepository.save(group);
        }).orElseThrow(GroupNotFoundException::new);
        return ResponseEntity.ok(entity);
    }

    @PreAuthorize("hasAuthority('G0007')")
    @DeleteMapping(name = "移除资源", value = "/{id}/resources")
    public HttpEntity<?> remove(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        Group entity = groupRepository.findById(id).map(group -> {
            List<Resource> resources = resourceRepository.findAllById(ids);
            group.getResources().removeAll(resources);
            return groupRepository.save(group);
        }).orElseThrow(GroupNotFoundException::new);
        return ResponseEntity.ok(entity);
    }
}
