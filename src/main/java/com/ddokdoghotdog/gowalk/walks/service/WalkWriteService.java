package com.ddokdoghotdog.gowalk.walks.service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;
import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths.PathPoint;
import com.ddokdoghotdog.gowalk.walks.repository.WalkPathsRepository;
import com.ddokdoghotdog.gowalk.walks.repository.WalkRepository;
import com.ddokdoghotdog.gowalk.walks.util.DistanceCalculator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class WalkWriteService {
    private final WalkRedisService walkRedisService;
    private final WalkReadService walkReadService;
    private final PetRepository petRepository;
    private final WalkRepository walkRepository;
    private final WalkPathsRepository walkPathRepository;

    public WalkDTO.WalkStartResponse startWalk(WalkDTO.WalkStartRequest walkStartDTO) throws JsonProcessingException {

        List<Pet> pets = petRepository.findAllByIdInAndMemberId(walkStartDTO.getDogs(), walkStartDTO.getMemberId());
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Walk walk = Walk.builder()
                .startTime(startTime)
                .totalDistance(0L)
                .build();
        pets.forEach(walk::addPet);
        walkRepository.save(walk);

        PathPoint initialLocation = WalkPaths.PathPoint.from(
                walkStartDTO.getLatitude(),
                walkStartDTO.getLongitude());
        walkRedisService.initPath(walk.getId(), initialLocation);
        return WalkDTO.WalkStartResponse.of(walk);
    }

    // 기록, 결과에 현재정보 칼로리 m등 정보주면 좋을듯
    public void recordWalkPath(WalkDTO.WalkUpdateRequest walkUpdateDTO) throws JsonProcessingException {
        Long memberId = walkUpdateDTO.getMemberId();
        Long walkId = walkUpdateDTO.getWalkId();
        Walk walk = walkReadService.getWalkByIdAndMemberId(walkId, memberId);

        // 거리 계산
        double prevDistance = walk.getTotalDistance();
        double newDistance = DistanceCalculator.calculateDistance(walkUpdateDTO.getWalkPaths());
        double totalDistance = prevDistance + newDistance;

        // 칼로리 계산

        // 각 강아지들마다 칼로리 계산 후 oracle DB업데이트(petwalk.total_calories)
        // 거리 계산 후 db업데이트
        walkRedisService.updateWalkPath(walkId, walkUpdateDTO.getWalkPaths());
    }

    public WalkDTO.WalkEndResponse endWalk(WalkDTO.WalkEndRequest walkEndDTO) throws JsonProcessingException {
        Long memberId = walkEndDTO.getMemberId();
        Long walkId = walkEndDTO.getWalkId();
        Walk walk = walkReadService.getWalkByIdAndMemberId(walkId, memberId);
        PathPoint finalLocation = WalkPaths.PathPoint.from(walkEndDTO.getLatitude(), walkEndDTO.getLongitude());

        List<PathPoint> allPoints = walkRedisService.getAllPathPoints(walkId);
        allPoints.add(finalLocation);
        allPoints.sort(Comparator.comparing(PathPoint::getRecordTime));

        // MongoDB저장
        WalkPaths walkPaths = WalkPaths.builder()
                .walkId(walkId)
                .paths(allPoints)
                .build();
        walkPathRepository.save(walkPaths);

        // Oracle 업데이트
        walk.toBuilder()
                .endTime(new Timestamp(System.currentTimeMillis()))
                // totalDistance totalCalories
                .build();

        walkRepository.save(walk);
        walkRedisService.cleanupRedisData(walkId);
        return null;
    }
}
