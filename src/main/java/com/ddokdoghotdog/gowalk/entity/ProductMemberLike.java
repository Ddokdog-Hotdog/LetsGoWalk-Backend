package com.ddokdoghotdog.gowalk.entity;

import java.sql.Date;

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
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_member_likes")
public class ProductMemberLike {

    @EmbeddedId
    private ProductMemberLikeId id;

    @ManyToOne
    @MapsId("productid")
    @JoinColumn(name = "productid", nullable = false)
    private Product product;

    @ManyToOne
    @MapsId("memberid")
    @JoinColumn(name = "memberid", nullable = false)
    private Member member;

    @Setter
    @Getter
    @Embeddable
    public static class ProductMemberLikeId {
        private Long productid;
        private Long memberid;
    }
}

