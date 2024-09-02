package com.ddokdoghotdog.gowalk.entity;

import java.sql.Date;

import com.ddokdoghotdog.gowalk.global.entity.BaseTimeEntity;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO;

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
import lombok.Setter;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pets")
public class Pet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "breedid", nullable = false)
    private Breed breed;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dateofbirth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "neutering", nullable = false)
    private Boolean neutering;

    @Column(name = "profileimageurl")
    private String profileImageUrl;

    public void updatePet(PetDTO.Update petDTO, Breed breed) {
        this.breed = breed;
        this.name = petDTO.getName();
        this.weight = petDTO.getWeight();
        this.neutering = petDTO.getNeutering();
        this.profileImageUrl = petDTO.getProfileImageUrl();
    }
}
