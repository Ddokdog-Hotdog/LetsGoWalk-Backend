package com.ddokdoghotdog.gowalk.quests.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.quests.dto.QuestDTO;
import com.ddokdoghotdog.gowalk.quests.service.QuestService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quests")
public class QuestController {

    private final QuestService questService;
    
    @GetMapping("")
    public List<QuestDTO> getVisibleQuestsAndAchievementsForToday(@RequestParam Long memberId) {
        return questService.getVisibleQuestsAndAchievementsForToday(memberId);
    }

    @PutMapping("")
    public void completeQuest(@RequestParam Long memberId, @RequestParam Long questId) {
        questService.completeQuest(memberId, questId);
    }

    @PutMapping("")
    public void rewardPoints(@RequestParam Long memberId, @RequestParam Long questId) {
        questService.rewardPoints(memberId, questId);
    }
}
