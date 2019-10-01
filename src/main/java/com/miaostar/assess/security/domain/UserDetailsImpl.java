package com.miaostar.assess.security.domain;

import com.miaostar.assess.system.entity.Group;
import com.miaostar.assess.system.entity.Resource;
import com.miaostar.assess.system.entity.Role;
import com.miaostar.assess.system.entity.User;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDetailsImpl implements UserDetails, CredentialsContainer {

    private User user;

    private String password;

    public UserDetailsImpl(User user) {
        this.user = user;
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(user.getGroups().stream().map(Group::getResources), user.getRoles().stream().map(Role::getResources))
                .flatMap(Collection::stream).map(Resource::getCode)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnable();
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
