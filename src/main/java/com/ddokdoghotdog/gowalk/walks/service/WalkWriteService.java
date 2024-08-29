package com.ddokdoghotdog.gowalk.walks.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;
import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.repository.WalkPathsRepository;
import com.ddokdoghotdog.gowalk.walks.repository.WalkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WalkWriteService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PetRepository petRepository;
    private final WalkRepository walkRepository;
    private final WalkPathsRepository walkPathRepository;

    @Transactional
    public WalkDTO.WalkStartResponse startWalk(WalkDTO.WalkStartRequest walkStartDTO) {
        List<Pet> pets = petRepository.findAllById(walkStartDTO.getDogs());

        Walk walk = Walk.builder()
                .startTime(new Date())
                .build();

        for (Pet pet : pets) {
            walk.addPet(pet);
        }

        walkRepository.save(walk);

        WalkDTO.WalkStartResponse response = null;
        return null;
    }

    public WalkPaths saveWalk(WalkPaths walk) {
        return walkPathRepository.save(walk);
    }

    public void deleteWalk(String id) {
        walkPathRepository.deleteById(id);
    }
}
