package com.ddokdoghotdog.gowalk.quests.service;


import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Quest;
import com.ddokdoghotdog.gowalk.entity.QuestAchievement;
import com.ddokdoghotdog.gowalk.quests.repository.QuestAchievementRepository;
import com.ddokdoghotdog.gowalk.quests.repository.QuestRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final QuestAchievementRepository questAchievementRepository;
    private final MemberRepository memberRepository;


    public Map<String, Object> getQuestsWithStatus(Long memberId) {
        List<Quest> quests = questRepository.findAll();  
        List<QuestAchievement> achievements = getTodayAchievements(memberId);  

        Map<String, Object> result = new HashMap<>();
        result.put("quests", quests);
        result.put("achievements", achievements);
        return result;   
    }
    
    public List<QuestAchievement> getTodayAchievements(Long memberId) {
        Date today = new Date(System.currentTimeMillis());
        return questAchievementRepository.findByMemberIdAndRewardDate(memberId, today);
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    @Transactional
    public void rewardPoints(Long memberId, Long questId) {

        QuestAchievement.QuestAchievementId achievementId = new QuestAchievement.QuestAchievementId();
        achievementId.setMemberid(memberId);
        achievementId.setQuestid(questId);
        
        Optional<QuestAchievement> achievementOpt = questAchievementRepository.findById(achievementId);

        if (achievementOpt.isPresent()) {
            QuestAchievement achievement = achievementOpt.get();
            if (!achievement.getIsRewarded()) {
                
                Member member = achievement.getMember();
                Quest quest = achievement.getQuest();
                member.setPoint(member.getPoint() + quest.getAchievementPoints());

                achievement.setIsRewarded(true);
                achievement.setRewardDate(new Date(System.currentTimeMillis()));
                memberRepository.save(member);
                questAchievementRepository.save(achievement);
            }
        }
    }
    
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteUnrewardedAchievements() {
        questAchievementRepository.deleteByIsRewardedFalse();
    }

}
