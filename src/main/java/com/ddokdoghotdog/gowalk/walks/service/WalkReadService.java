package com.ddokdoghotdog.gowalk.walks.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.DailyWalkSummaryRequest;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.DailyWalkSummaryResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.MonthlyWalkSummaryRequest;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.MonthlyWalkSummaryResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.NearbyHotplaceResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.NearbyWalkPathsRequest;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.WalkSummary;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.repository.WalkPathsRepository;
import com.ddokdoghotdog.gowalk.walks.repository.WalkRepository;
import com.ddokdoghotdog.gowalk.walks.util.IntersectionFinder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WalkReadService {
    private final WalkPathsRepository walkPathRepository;
    private final WalkRepository walkRepository;
    private final IntersectionFinder intersectionFinder;

    private static final int INIT_DISTANCE = 1000; // 첫 검색 1000m
    private static final int DISTANCE_INCREASE_INTERVAL = 500; // 늘어날 거리
    private static final int MAX_DISTANCE = 2000;
    private static final int RESULT_LIMIT = 100;

    public Walk getWalkById(Long walkId) {
        return walkRepository.findWalkWithPetsById(walkId)
                .orElseThrow(() -> new BusinessException(ErrorCode.WALK_NOT_FOUND));
    }

    public Walk getWalkByIdAndMemberId(Long walkId, Long memberId) {
        return walkRepository.findByIdAndMemberId(walkId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.WALK_NOT_FOUND));
    }

    public DailyWalkSummaryResponse getDailyWalk(DailyWalkSummaryRequest DailyRequestDTO) {
        List<Walk> walks = walkRepository.findAllByPetOwnerMemberIdAndDay(DailyRequestDTO.getMemberId(),
                DailyRequestDTO.getYear(), DailyRequestDTO.getMonth(), DailyRequestDTO.getDay());
        List<Long> walkIds = walks.stream()
                .map(Walk::getId)
                .collect(Collectors.toList());
        List<WalkPaths> walkPathsList = walkPathRepository.findAllByWalkIdIn(walkIds);

        List<WalkSummary> walkSummaries = WalkSummary.from(walks, walkPathsList);
        DailyWalkSummaryResponse dailyWalkSummaries = DailyWalkSummaryResponse.of(walkSummaries,
                DailyRequestDTO.toDate());
        return dailyWalkSummaries;
    }

    public MonthlyWalkSummaryResponse getMonthlyWalk(
            MonthlyWalkSummaryRequest MonthlyRequestDTO) {
        int year = MonthlyRequestDTO.getYear();
        int month = MonthlyRequestDTO.getMonth();

        List<Walk> walks = walkRepository.findAllByPetOwnerMemberIdAndMonth(MonthlyRequestDTO.getMemberId(),
                year, month);
        List<Long> walkIds = walks.stream()
                .map(Walk::getId)
                .collect(Collectors.toList());
        List<WalkPaths> walkPathsList = walkPathRepository.findAllByWalkIdIn(walkIds);

        return MonthlyWalkSummaryResponse.of(walks, walkPathsList, year, month);
    }

    public List<WalkPaths> getNearbyWalkPaths(NearbyWalkPathsRequest pointDTO) {

        // 주변 1km 탐색
        int maxDistance = 1000;
        Pageable limit = PageRequest.of(0, RESULT_LIMIT);
        Timestamp fromDate = new Timestamp(System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)); // 30일 전
        GeoJsonPoint location = new GeoJsonPoint(pointDTO.getLongitude(), pointDTO.getLatitude());

        return searchIncrementalDistance(location, maxDistance, fromDate, limit);
    }

    public List<NearbyHotplaceResponse> getNearbyHotspots(NearbyWalkPathsRequest pointDTO) {
        List<WalkPaths> walkPaths = getNearbyWalkPaths(pointDTO);
        return NearbyHotplaceResponse.of(intersectionFinder.findHotspot(walkPaths));
    }

    private List<WalkPaths> searchIncrementalDistance(GeoJsonPoint location, int curDistance, Timestamp fromDate,
            Pageable limit) {
        List<WalkPaths> results = walkPathRepository.findByLocationNear(location, curDistance, fromDate, limit);
        if (results.isEmpty() && curDistance < MAX_DISTANCE) {
            return searchIncrementalDistance(location, curDistance + DISTANCE_INCREASE_INTERVAL, fromDate, limit);
        }

        return results;
    }
}