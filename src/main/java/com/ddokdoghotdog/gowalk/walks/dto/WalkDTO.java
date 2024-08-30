package com.ddokdoghotdog.gowalk.walks.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.global.entity.BaseMemberIdDTO;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WalkDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalkStartRequest extends BaseMemberIdDTO {

        private List<Long> dogs;
        private double latitude;
        private double longitude;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalkStartResponse {
        private Long walkId;
        private List<PetDTO.Response> dogs;
        private Timestamp startTime;

        public static WalkStartResponse of(Walk walk) {
            List<PetDTO.Response> dogResponse = walk.getPetWalks().stream()
                    .map(petWalk -> PetDTO.Response.of(petWalk.getPet()))
                    .collect(Collectors.toList());

            return WalkStartResponse.builder()
                    .walkId(walk.getId())
                    .startTime(walk.getStartTime())
                    .dogs(dogResponse)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalkEndRequest extends BaseMemberIdDTO {
        private Long walkId;
        private double latitude;
        private double longitude;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalkEndResponse {
        private Long walkId;
        private Timestamp startTime;
        private Timestamp endTime;
        private int totalDuration; // 초 단위
        private int totalDistance; // M
        private List<PetResult> dogs;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetResult {
        private PetDTO.Response pet;
        private int caloriesBurned;
    }

}
