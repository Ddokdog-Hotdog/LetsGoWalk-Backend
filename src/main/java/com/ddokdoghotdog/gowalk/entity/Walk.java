package com.ddokdoghotdog.gowalk.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "walks")
public class Walk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "starttime")
    private Timestamp startTime;

    @Column(name = "endtime")
    private Timestamp endTime;

    @Column(name = "total_distance")
    private Long totalDistance;

    @OneToMany(mappedBy = "walk", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PetWalk> petWalks = new HashSet<>();

    public void addPet(Pet pet) {
        PetWalk petWalk = PetWalk.builder()
                .pet(pet)
                .walk(this)
                .totalCalories(0.0)
                .build();
        petWalks.add(petWalk);
    }

    public List<Pet> getPets() {
        return this.getPetWalks().stream()
                .map(petWalk -> petWalk.getPet())
                .collect(Collectors.toList());
    }
}
