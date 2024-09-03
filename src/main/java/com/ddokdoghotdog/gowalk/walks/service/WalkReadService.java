package com.ddokdoghotdog.gowalk.walks.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.DailyWalkSummaryResponse;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.repository.WalkPathsRepository;
import com.ddokdoghotdog.gowalk.walks.repository.WalkRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WalkReadService {
        private final WalkPathsRepository walkPathRepository;
        private final WalkRepository walkRepository;

        public Walk getWalkById(Long walkId) {
                return walkRepository.findWalkWithPetsById(walkId)
                                .orElseThrow(() -> new BusinessException(ErrorCode.WALK_NOT_FOUND));
        }

        public Walk getWalkByIdAndMemberId(Long walkId, Long memberId) {
                return walkRepository.findByIdAndMemberId(walkId, memberId)
                                .orElseThrow(() -> new BusinessException(ErrorCode.WALK_NOT_FOUND));
        }

        public DailyWalkSummaryResponse getDailyWalk(WalkSummaryDTO.DailyWalkSummaryRequest DailyRequestDTO) {
                List<Walk> walks = walkRepository.findAllByPetOwnerMemberIdAndDay(DailyRequestDTO.getMemberId(),
                                DailyRequestDTO.getYear(), DailyRequestDTO.getMonth(), DailyRequestDTO.getDay());
                List<Long> walkIds = walks.stream()
                                .map(Walk::getId)
                                .collect(Collectors.toList());
                List<WalkPaths> walkPathsList = walkPathRepository.findAllByWalkIdIn(walkIds);

                List<WalkSummaryDTO.WalkSummary> walkSummaries = WalkSummaryDTO.WalkSummary.from(walks, walkPathsList);
                DailyWalkSummaryResponse dailyWalkSummaries = DailyWalkSummaryResponse.of(walkSummaries,
                                DailyRequestDTO.toDate());
                return dailyWalkSummaries;
        }

        public WalkSummaryDTO.MonthlyWalkSummaryResponse getMonthlyWalk(
                        WalkSummaryDTO.MonthlyWalkSummaryRequest MonthlyRequestDTO) {
                int year = MonthlyRequestDTO.getYear();
                int month = MonthlyRequestDTO.getMonth();

                List<Walk> walks = walkRepository.findAllByPetOwnerMemberIdAndMonth(MonthlyRequestDTO.getMemberId(),
                                year, month);
                List<Long> walkIds = walks.stream()
                                .map(Walk::getId)
                                .collect(Collectors.toList());
                List<WalkPaths> walkPathsList = walkPathRepository.findAllByWalkIdIn(walkIds);

                return WalkSummaryDTO.MonthlyWalkSummaryResponse.of(walks, walkPathsList, year, month);
        }

        public List<WalkPaths> getNearbyWalkPaths(WalkSummaryDTO.NearbyWalkPathsRequest pointDTO) {

                // 주변 1km 탐색
                int maxDistance = 1000;
                Timestamp fromDate = new Timestamp(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000); // 30일 전
                GeoJsonPoint location = new GeoJsonPoint(pointDTO.getLongitude(), pointDTO.getLatitude());
                return walkPathRepository.findByLocationNear(location, maxDistance, fromDate);
        }
}