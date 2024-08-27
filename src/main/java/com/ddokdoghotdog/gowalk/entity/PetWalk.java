package com.ddokdoghotdog.gowalk.entity;

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

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "petwalk")
public class PetWalk {

    @EmbeddedId
    private PetWalkId id;

    @ManyToOne
    @MapsId("petid")
    @JoinColumn(name = "petid", nullable = false)
    private Pet pet;

    @ManyToOne
    @MapsId("walkid")
    @JoinColumn(name = "walkid", nullable = false)
    private Walk walk;

    @Embeddable
    public static class PetWalkId {
        private Long petid;
        private Long walkid;

    }
}

