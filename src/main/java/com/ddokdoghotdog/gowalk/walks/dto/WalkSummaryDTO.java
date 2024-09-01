package com.ddokdoghotdog.gowalk.walks.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.global.entity.BaseMemberIdDTO;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WalkSummaryDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalkTest {
        private Long walkId;
        private Timestamp startTime;
        private Timestamp endTime;
        private Long duration;
        private Long distance;
        private List<PetDTO.Response> dogs;

        public static WalkTest of(Walk walk) {
            List<PetDTO.Response> dogResponse = walk.getPetWalks().stream()
                    .map(petWalk -> PetDTO.Response.of(petWalk.getPet()))
                    .collect(Collectors.toList());
            Long durationInSeconds = ((walk.getEndTime().getTime() - walk.getStartTime().getTime()) / 1000);

            return WalkTest.builder()
                    .walkId(walk.getId())
                    .startTime(walk.getStartTime())
                    .endTime(walk.getEndTime())
                    .duration(durationInSeconds)
                    .distance(walk.getTotalDistance())
                    .dogs(dogResponse)
                    .build();

        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalkSummary {
        private Long walkId;
        private Timestamp startTime;
        private Timestamp endTime;
        private int duration;
        private int distance;
        private List<WalkPaths.PathPoint> route;
        private List<WalkDTO.PetResult> dogs;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyWalkSummaryRequest extends BaseMemberIdDTO {
        private int year;
        private int month;
        private int day;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyWalkSummaryResponse {
        private Date date;
        private List<WalkSummary> walks;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyWalkSummaryRequest extends BaseMemberIdDTO {
        private int year;
        private int month;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyWalkSummaryResponse {
        private int year;
        private int month;
        private List<DailyWalkSummaryResponse> dailyWalks;
    }
}
