package com.ddokdoghotdog.gowalk.walks.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.global.entity.BaseMemberIdDTO;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths.PathPoint;

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
    public static class WalkUpdateRequest extends BaseMemberIdDTO {
        private Long walkId;
        private List<PathPoint> walkPaths;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalkUpdateResponse {
        private Long walkId;
        private Long totalDistance;
        private List<PetResult> dogs;

        public static WalkUpdateResponse of(Walk walk) {
            List<PetResult> petResults = PetResult.petWalkResultOf(walk);
            return WalkUpdateResponse.builder()
                    .walkId(walk.getId())
                    .totalDistance(walk.getTotalDistance())
                    .dogs(petResults)
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
        private Long totalDistance; // M
        private List<PetResult> dogs;

        public static WalkEndResponse of(Walk walk) {
            int totalDuration = (int) (walk.getEndTime().getTime() - walk.getStartTime().getTime()) / 1000;

            return WalkEndResponse.builder()
                    .walkId(walk.getId())
                    .startTime(walk.getStartTime())
                    .endTime(walk.getEndTime())
                    .totalDuration(totalDuration)
                    .totalDistance(walk.getTotalDistance())
                    .dogs(PetResult.petWalkResultOf(walk))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetResult {
        private PetDTO.Response pet;
        private double caloriesBurned;

        public static List<PetResult> petWalkResultOf(Walk walk) {
            return walk.getPetWalks().stream()
                    .map(petWalk -> {
                        Pet pet = petWalk.getPet();
                        double caloriesBurned = petWalk.getTotalCalories();
                        return PetResult.builder()
                                .pet(PetDTO.Response.of(pet))
                                .caloriesBurned(caloriesBurned)
                                .build();
                    })
                    .collect(Collectors.toList());
        }
    }

}
