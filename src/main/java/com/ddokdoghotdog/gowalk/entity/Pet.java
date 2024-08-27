package com.ddokdoghotdog.gowalk.entity;


import java.sql.Date;

import com.ddokdoghotdog.gowalk.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "pets")
public class Pet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "memberid", nullable = false)
    private Long memberid;

    @Column(name = "breedid", nullable = false)
    private Long breedid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dateofbirth")
    private Date dateOfBirth;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "neutering", nullable = false)
    private Boolean neutering;

    @Column(name = "profileimageurl", nullable = false)
    private String profileImageUrl;

}
