package com.ddokdoghotdog.gowalk.walks.dto;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.global.entity.BaseMemberIdDTO;
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
        private Long duration;
        private Long distance;
        private List<WalkPaths.PathPoint> route;
        private List<WalkDTO.PetResult> dogs;

        public static WalkSummary of(Walk walk, WalkPaths walkPaths) {
            long duration = (walk.getEndTime().getTime() - walk.getStartTime().getTime()) / 1000;

            return WalkSummary.builder()
                    .walkId(walk.getId())
                    .startTime(walk.getStartTime())
                    .endTime(walk.getEndTime())
                    .duration(duration)
                    .distance(walk.getTotalDistance())
                    .route(walkPaths.getPaths())
                    .dogs(WalkDTO.PetResult.petWalkResultOf(walk))
                    .build();
        }

        public static List<WalkSummaryDTO.WalkSummary> from(List<Walk> walks, List<WalkPaths> walkPathsList) {
            return walks.stream().map(walk -> {
                WalkPaths walkPaths = walkPathsList.stream()
                        .filter(paths -> paths.getWalkId().equals(walk.getId()))
                        .findFirst()
                        .orElse(null);
                if (walkPaths != null) {
                    return WalkSummaryDTO.WalkSummary.of(walk, walkPaths);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyWalkSummaryRequest extends BaseMemberIdDTO {
        private int year;
        private int month;
        private int day;

        public Date toDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, this.year);
            calendar.set(Calendar.MONTH, this.month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, this.day);

            return new Date(calendar.getTimeInMillis());
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyWalkSummaryResponse {
        private Date date;
        private List<WalkSummary> walks;

        public static DailyWalkSummaryResponse of(List<WalkSummary> walks, Date date) {
            return DailyWalkSummaryResponse.builder()
                    .date(date)
                    .walks(walks)
                    .build();
        }
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

        public static MonthlyWalkSummaryResponse of(List<Walk> walks, List<WalkPaths> walkPathsList, int year,
                int month) {

            // 날짜별 산책
            Map<Date, List<Walk>> walksByDate = walks.stream()
                    .collect(Collectors.groupingBy(walk -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(walk.getStartTime());
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        return cal.getTime();
                    }));

            // 날짜별 산책 DTO 생성
            List<DailyWalkSummaryResponse> dailyWalkSummaries = walksByDate.entrySet().stream()
                    .map(entry -> {
                        Date date = entry.getKey();
                        List<Walk> dailyWalksForDate = entry.getValue();
                        List<WalkSummaryDTO.WalkSummary> walkSummaries = WalkSummaryDTO.WalkSummary
                                .from(dailyWalksForDate, walkPathsList);
                        return DailyWalkSummaryResponse.of(walkSummaries, date);
                    })
                    .collect(Collectors.toList());

            // 날짜별 산책 정렬
            dailyWalkSummaries.sort(Comparator.comparing(DailyWalkSummaryResponse::getDate));

            return MonthlyWalkSummaryResponse.builder()
                    .year(year)
                    .month(month)
                    .dailyWalks(dailyWalkSummaries)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NearbyWalkPathsRequest {
        private double latitude;
        private double longitude;

    }
}
