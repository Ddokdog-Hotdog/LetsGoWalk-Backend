package com.ddokdoghotdog.gowalk.walks.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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
    public static class DailyWalkSummary {
        private Date date;
        private List<WalkSummary> walks;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyWalkSummary {
        private int year;
        private int month;
        private List<DailyWalkSummary> dailyWalks;
    }
}
