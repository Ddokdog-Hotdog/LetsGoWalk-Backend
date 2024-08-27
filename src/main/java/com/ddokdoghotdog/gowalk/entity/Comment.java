package com.ddokdoghotdog.gowalk.entity;

import java.sql.Date;

import com.ddokdoghotdog.gowalk.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberid", nullable = false)
    private Member member;

    @Column(name = "postid")
    private Long postid;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "commentid", nullable = true)
    private Long commentid;

}
