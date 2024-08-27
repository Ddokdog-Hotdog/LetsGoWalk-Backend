package com.ddokdoghotdog.gowalk.entity;

import java.sql.Date;

import com.ddokdoghotdog.gowalk.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "members")
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

//	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleid", nullable = false)
    private Long role;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "memberkey", nullable = false)
    private String memberKey;

    @Column(name = "socialprovider", nullable = false)
    private String socialProvider;

    @Column(name = "prifileimageurl")
    private String profileImageUrl;

    @Column(name = "dateofbirth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "point", nullable = false)
    private Long point;
}