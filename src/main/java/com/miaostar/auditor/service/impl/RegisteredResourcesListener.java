package com.miaostar.auditor.service.impl;

import com.miaostar.auditor.entity.Resource;
import com.miaostar.auditor.repository.ResourceRepository;
import com.miaostar.auditor.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class RegisteredResourcesListener implements ApplicationListener<ContextRefreshedEvent> {

    private RequestMappingHandlerMapping mapping;

    private ResourceRepository resourceRepository;

    private RoleRepository roleRepository;

    public RegisteredResourcesListener(RequestMappingHandlerMapping mapping,
                                       ResourceRepository resourceRepository,
                                       RoleRepository roleRepository) {
        this.mapping = mapping;
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Set<Resource> resources = mapping.getHandlerMethods().values().stream()
                .filter(method -> {
                    RequestMapping requestMapping = method.getMethodAnnotation(RequestMapping.class);
                    return Objects.nonNull(requestMapping) && !StringUtils.isEmpty(requestMapping.name());
                })
                .map(method -> {
                    //获取控制器方法前缀
                    RequestMapping annotation = method.getMethod().getDeclaringClass().getAnnotation(RequestMapping.class);

                    String prefix = "";

                    if (Objects.nonNull(annotation)) {
                        String[] value = annotation.value();
                        if (value.length > 0) {
                            prefix = value[0];
                        }
                    }

                    RequestMapping requestMapping = method.getMethodAnnotation(RequestMapping.class);

                    //通过控制器方法生成系统资源
                    String[] paths = Objects.requireNonNull(requestMapping).path();
                    RequestMethod[] methods = requestMapping.method();
                    String name = requestMapping.name();

                    PreAuthorize authorize = method.getMethodAnnotation(PreAuthorize.class);

                    String code = null;

                    if (Objects.nonNull(authorize) && !StringUtils.isEmpty(authorize.value())) {
                        code = authorize.value().split("'")[1];
                    }

                    Resource resource = new Resource();
                    resource.setName(name);
                    resource.setMethod(methods.length == 0 ? "*" : methods[0].toString());
                    resource.setPath(prefix + (paths.length > 0 ? paths[0] : ""));
                    resource.setCode(code);

                    return resource;
                })
                .filter(resource -> {
                    Example<Resource> example = Example.of(resource,
                            ExampleMatcher.matching().withIgnoreCase("roles"));
                    return !resourceRepository.exists(example);
                }).collect(Collectors.toSet());

        log.debug(
                "系统资源如下:{}",
                resources.stream()
                        .sorted(Comparator.comparing(Resource::getCode))
                        .map(resource -> String.format("[编号:%s,名称:%s]", resource.getCode(), resource.getName()))
                        .collect(Collectors.joining(","))
        );

        List<Resource> list = resourceRepository.saveAll(resources);

        roleRepository.findByName("ADMIN").ifPresent(role -> {
            role.getResources().addAll(list);
            roleRepository.save(role);
        });

    }
}

