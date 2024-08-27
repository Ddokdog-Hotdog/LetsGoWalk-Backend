package com.ddokdoghotdog.gowalk.entity;

import java.sql.Date;

import com.ddokdoghotdog.gowalk.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "memberid", nullable = false)
    private Long memberid;

    @Column(name = "boardid", nullable = false)
    private Long boardid;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

}
