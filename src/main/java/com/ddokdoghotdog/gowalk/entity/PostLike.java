package com.ddokdoghotdog.gowalk.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_likes")
public class PostLike {

    @EmbeddedId
    private PostLikeId id;

    @ManyToOne
    @MapsId("memberid")
    @JoinColumn(name = "memberid", nullable = false)
    private Member member;

    @ManyToOne
    @MapsId("postid")
    @JoinColumn(name = "postid", nullable = false)
    private Post post;

    // Embedded ID class
    @Embeddable
    public static class PostLikeId {
        private Long memberid;
        private Long postid;

    }
}
