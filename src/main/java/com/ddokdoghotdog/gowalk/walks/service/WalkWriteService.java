package com.ddokdoghotdog.gowalk.walks.service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;
import com.ddokdoghotdog.gowalk.quests.service.QuestService;
import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths.PathPoint;
import com.ddokdoghotdog.gowalk.walks.repository.PetWalkRepository;
import com.ddokdoghotdog.gowalk.walks.repository.WalkPathsRepository;
import com.ddokdoghotdog.gowalk.walks.repository.WalkRepository;
import com.ddokdoghotdog.gowalk.walks.util.CalorieCalculator;
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
    private final PetWalkRepository petWalkRepository;
    private final QuestService questService;

    public WalkDTO.WalkStartResponse startWalk(WalkDTO.WalkStartRequest walkStartDTO)
            throws JsonProcessingException {

        List<Pet> pets = petRepository.findAllByIdInAndMemberId(walkStartDTO.getDogs(),
                walkStartDTO.getMemberId());
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

    public WalkDTO.WalkUpdateResponse recordWalkPath(WalkDTO.WalkUpdateRequest walkUpdateDTO)
            throws JsonProcessingException {
        Long memberId = walkUpdateDTO.getMemberId();
        Long walkId = walkUpdateDTO.getWalkId();
        Walk walk = walkReadService.getWalkByIdAndMemberId(walkId, memberId);

        walk = updateDistance(walk, walkUpdateDTO.getWalkPaths());
        walk = updateCalories(walk, walk.getTotalDistance());
        walkRedisService.updateWalkPath(walkId, walkUpdateDTO.getWalkPaths());

        return WalkDTO.WalkUpdateResponse.of(walk);
    }

    public WalkDTO.WalkEndResponse endWalk(WalkDTO.WalkEndRequest walkEndDTO) throws JsonProcessingException {
        Long memberId = walkEndDTO.getMemberId();
        Long walkId = walkEndDTO.getWalkId();
        Walk walk = walkReadService.getWalkByIdAndMemberId(walkId, memberId);
        PathPoint finalLocation = WalkPaths.PathPoint.from(walkEndDTO.getLatitude(), walkEndDTO.getLongitude());

        // 경로 정렬
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
        walk = walk.toBuilder()
                .endTime(new Timestamp(System.currentTimeMillis()))
                .build();
        walkRepository.save(walk);

        walkRedisService.cleanupRedisData(walkId);
        validateWalkQuest(walk, memberId);
        return WalkDTO.WalkEndResponse.of(walk);
    }

    private Walk updateDistance(Walk walk, List<WalkPaths.PathPoint> newPathPoints) {
        Long prevDistance = walk.getTotalDistance();
        Long newDistance = DistanceCalculator.calculateDistance(newPathPoints);
        Long totalDistance = prevDistance + newDistance;
        Walk updatewalk = walk.toBuilder()
                .totalDistance((long) totalDistance)
                .build();
        walkRepository.save(updatewalk);
        return updatewalk;
    }

    private Walk updateCalories(Walk walk, Long totalDistance) {
        walk.getPetWalks().forEach(petWalk -> {
            Pet pet = petWalk.getPet();
            double petWeight = pet.getWeight();
            double totalCalories = CalorieCalculator.calculateCalories(petWeight, totalDistance);
            petWalk = petWalk.toBuilder()
                    .totalCalories(totalCalories)
                    .build();
            petWalkRepository.save(petWalk);
        });
        return walk;
    }

    @Async
    public void validateWalkQuest(Walk walk, Long memberId) {
        // 선행조건
        Long walkQuestId = 2L;
        Long minimumDistance = 500L;
        Long minimumDuration = 60L;

        Long totalDuration = (walk.getEndTime().getTime() - walk.getStartTime().getTime()) / 1000;
        if (walk.getTotalDistance() >= minimumDistance && totalDuration >= minimumDuration) {
            questService.completeQuest(memberId, walkQuestId);
        }
    }
}
