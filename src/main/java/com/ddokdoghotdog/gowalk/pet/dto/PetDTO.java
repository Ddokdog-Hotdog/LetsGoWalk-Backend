package com.ddokdoghotdog.gowalk.pet.dto;

import java.sql.Date;

import com.ddokdoghotdog.gowalk.entity.Breed;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.global.entity.BaseMemberIdDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PetDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest extends BaseMemberIdDTO {
        private Long breedId;
        private String name;
        private Date dateOfBirth;
        private String gender;
        private Double weight;
        private Boolean neutering;
        private String profileImageUrl;

        public Pet toEntity(Member member, Breed breed) {
            return Pet.builder()
                    .member(member)
                    .breed(breed)
                    .name(name)
                    .dateOfBirth(dateOfBirth)
                    .gender(gender)
                    .weight(weight)
                    .neutering(neutering)
                    .profileImageUrl(profileImageUrl)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update extends BaseMemberIdDTO {
        private Long petId;
        private Long breedId;
        private String name;
        private Double weight;
        private Boolean neutering;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long petId;
        private Long breedId;
        private String breedName;
        private String name;
        private Date dateOfBirth;
        private String gender;
        private Double weight;
        private Boolean neutering;
        private String profileImageUrl;

        public static Response of(Pet pet) {
            return PetDTO.Response.builder()
                    .petId(pet.getId())
                    .breedId(pet.getBreed().getId())
                    .breedName(pet.getBreed().getName())
                    .name(pet.getName())
                    .dateOfBirth(pet.getDateOfBirth())
                    .gender(pet.getGender())
                    .weight(pet.getWeight())
                    .neutering(pet.getNeutering())
                    .profileImageUrl(pet.getProfileImageUrl())
                    .build();
        }
    }
}
