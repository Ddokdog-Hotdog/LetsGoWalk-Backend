package com.ddokdoghotdog.gowalk.walks.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Walk;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
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
        Walk walk = walkRepository.findByIdAndMemberId(walkId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.WALK_NOT_FOUND));

        return null;
    }

    public List<Walk> getMonthlyWalk(Long memberId, int year, int month) {
        return walkRepository.findAllByPetOwnerMemberIdAndMonth(memberId, year, month);
    }
}