package com.ddokdoghotdog.gowalk.quests.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.global.annotation.RequiredMemberId;
import com.ddokdoghotdog.gowalk.quests.dto.QuestDTO;
import com.ddokdoghotdog.gowalk.quests.service.QuestService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quests")
public class QuestController {

    private final QuestService questService;
    
    @GetMapping("")
    @RequiredMemberId
    public List<QuestDTO> getVisibleQuestsAndAchievementsForToday(Long memberId) {
        return questService.getVisibleQuestsAndAchievementsForToday(memberId);
    }

    @PostMapping("")
    public void completeQuest(@RequestParam(name = "memberId") Long memberId, @RequestParam(name = "questId") Long questId) {
        questService.completeQuest(memberId, questId);
    }

    @PutMapping("")
    @RequiredMemberId
    public void rewardPoints(@RequestParam(name = "questId") Long questId, Long memberId) {
    	questService.rewardPoints(memberId, questId);
    }
}
