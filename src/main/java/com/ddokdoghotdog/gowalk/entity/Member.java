package com.ddokdoghotdog.gowalk.entity;

import java.util.Date;
import java.util.List;

import com.ddokdoghotdog.gowalk.global.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleid", nullable = false)
    private UserRole role;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "nickname", length = 20)
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
    private String gender;

    @Column(name = "point", nullable = false)
    private Long point;
    
    @Column(name = "phoneNumber",unique = true, length = 50)
    private String phoneNumber;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pet> pets;
}