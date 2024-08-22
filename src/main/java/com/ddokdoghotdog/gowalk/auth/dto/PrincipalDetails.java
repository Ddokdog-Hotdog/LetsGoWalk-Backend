package com.ddokdoghotdog.gowalk.auth.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ddokdoghotdog.gowalk.auth.entity.Member;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PrincipalDetails implements OAuth2User, UserDetails {
    private final Member member;
    private final Map<String, Object> attributes;
    private final String attributeKey;

    @Override
    public java.util.Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().getAuthority()));
    }

    @Override
    public String getName() {
        return attributes.get(attributeKey).toString();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return member.getMemberKey();
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
        return true;
    }
}
