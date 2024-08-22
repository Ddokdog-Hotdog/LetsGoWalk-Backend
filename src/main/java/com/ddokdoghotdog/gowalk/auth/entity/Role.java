package com.ddokdoghotdog.gowalk.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    USER("ROLE_USER"),
    VETERINARIAN("ROLE_VETERINARIAN"),
    TRAINER("ROLE_TRAINER"),
    ADMIN("ROLE_ADMIN");

    private final String authority;
}
