package com.ddokdoghotdog.gowalk.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    
    @Builder
    public PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
        this.id = new PostLikeId(member.getId(), post.getId()); // ID 생성
    }

    // Embedded ID class
    @Embeddable
    public static class PostLikeId implements Serializable {
        private Long memberid;
        private Long postid;

        // Default constructor
        public PostLikeId() {}

        public PostLikeId(Long memberid, Long postid) {
            this.memberid = memberid;
            this.postid = postid;  
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PostLikeId that = (PostLikeId) o;
            return Objects.equals(memberid, that.memberid) &&
                   Objects.equals(postid, that.postid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(memberid, postid);
        }
    }
}
