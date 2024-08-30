package com.ddokdoghotdog.gowalk.quests.controller;


import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.quests.service.QuestService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quests")
public class QuestController {

    private final QuestService questService;

    // 퀘스트 목록과 오늘의 퀘스트 상태 가져오기
    @GetMapping
    public Map<String, Object> getQuestsWithStatus(@RequestParam Long memberId) {
        return questService.getQuestsWithStatus(memberId);
    }

    // 포인트 받기
    @PostMapping("/reward")
    public void rewardPoints(@RequestParam Long memberId, @RequestParam Long questId) {
        questService.rewardPoints(memberId, questId);
    }
}
