package com.ddokdoghotdog.gowalk.walks.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.walks.repository.WalkRepository;
import com.ddokdoghotdog.gowalk.walks.repository.WalkPathsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalkCleanupService {

    private final WalkRedisService walkRedisService;
    private final WalkRepository walkRepository;

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    @Transactional
    public void cleanupExpiredWalks() {
        log.info("만료된 walk를 삭제합니다.");

        // Redis에서 모든 활성 walk ID 가져오기
        Set<String> activeWalkIds = walkRedisService.getAllActiveWalkIds();

        // DB에서 모든 종료되지 않은 walk ID 가져오기
        List<Long> unfinishedWalkIds = walkRepository.findAllUnfinishedWalkIds();

        // DB에 있지만 Redis에 없는 walk ID 찾기
        for (Long walkId : unfinishedWalkIds) {
            if (!activeWalkIds.contains("walk:" + walkId + ":route")) {
                log.info("삭제된 walkId: {}", walkId);

                // walk 데이터 삭제
                walkRepository.deleteById(walkId);
            }
        }

        log.info("만료된 walk 삭제 완료.");
    }
}