package com.ddokdoghotdog.gowalk.walks.service;

import java.sql.Timestamp;
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
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkWriteService {
    private final WalkRedisService walkRedisService;
    private final PetRepository petRepository;
    private final WalkRepository walkRepository;
    private final WalkPathsRepository walkPathRepository;

    @Transactional
    public WalkDTO.WalkStartResponse startWalk(WalkDTO.WalkStartRequest walkStartDTO) throws JsonProcessingException {

        List<Pet> pets = petRepository.findAllByIdInAndMemberId(walkStartDTO.getDogs(), walkStartDTO.getMemberId());
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Walk walk = Walk.builder()
                .startTime(startTime)
                .build();
        pets.forEach(walk::addPet);
        walkRepository.save(walk);

        PathPoint initialLocation = WalkPaths.PathPoint.from(
                walkStartDTO.getLatitude(),
                walkStartDTO.getLongitude());
        walkRedisService.initPath(walk.getId(), initialLocation);
        return WalkDTO.WalkStartResponse.of(walk);
    }

    public WalkPaths saveWalk(WalkPaths walk) {
        return walkPathRepository.save(walk);
    }
}
