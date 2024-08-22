package com.ddokdoghotdog.gowalk.auth.entity;

import java.sql.Timestamp;

import com.ddokdoghotdog.gowalk.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true)
    private String memberKey;

    @Column(nullable = false)
    private String socialProvider;

    @Column(nullable = true)
    private String profileImageUrl;

    @Column()
    private Timestamp dateOfBirth;

    @Column()
    private String gender;

    @Column()
    private int point;
}