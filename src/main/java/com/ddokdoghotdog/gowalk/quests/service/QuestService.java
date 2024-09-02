package com.ddokdoghotdog.gowalk.quests.service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Quest;
import com.ddokdoghotdog.gowalk.entity.QuestAchievement;
import com.ddokdoghotdog.gowalk.entity.QuestAchievement.QuestAchievementId;
import com.ddokdoghotdog.gowalk.quests.dto.QuestAchievementDTO;
import com.ddokdoghotdog.gowalk.quests.dto.QuestDTO;
import com.ddokdoghotdog.gowalk.quests.repository.QuestAchievementRepository;
import com.ddokdoghotdog.gowalk.quests.repository.QuestRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final QuestAchievementRepository questAchievementRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void completeQuest(Long memberId, Long questId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new IllegalArgumentException("Quest not found"));

        QuestAchievementId id = new QuestAchievementId();
        id.setMemberid(memberId);
        id.setQuestid(questId);

        QuestAchievement achievement = QuestAchievement.builder()
                .id(id)
                .member(member)
                .quest(quest)
                .isRewarded(false)
                .rewardDate(null)
                .build();

        questAchievementRepository.save(achievement);
    }
    
    public List<Quest> getQuestList() {
        return questRepository.findByIsVisibleTrue();
    }
 
    public List<QuestDTO> getVisibleQuestsAndAchievementsForToday(Long memberId) {
    	 LocalDate today = LocalDate.now();

     // 모든 퀘스트 가져오기 (isVisible이 true인 퀘스트들)
        List<Quest> visibleQuests = questRepository.findByIsVisibleTrue();

        // 해당 멤버의 모든 퀘스트 달성 기록 가져오기
        List<QuestAchievement> achievements = questAchievementRepository.findByMemberId(memberId);

        // visibleQuests를 순회하면서 QuestDTO 리스트로 변환
        return visibleQuests.stream()
            .map(quest -> {
                Optional<QuestAchievement> optionalAchievement = achievements.stream()
                    .filter(a -> a.getQuest().getId().equals(quest.getId()))
                    .findFirst();

                boolean isCompleted = optionalAchievement.isPresent();
                boolean isRewarded = false;

                if (isCompleted) {
                    QuestAchievement achievement = optionalAchievement.get();
                    // rewardDate가 null이 아니고 오늘인 경우 보상이 이루어짐
                    if (achievement.getRewardDate() != null) {
                        LocalDate rewardDate = achievement.getRewardDate().toLocalDate();
                        if (today.equals(rewardDate)) {
                            isRewarded = true;
                        }
                    }
                }

                return QuestDTO.builder()
                    .questId(quest.getId())
                    .questName(quest.getName())
                    .description(quest.getDescription())
                    .questPoint(quest.getAchievementPoints().intValue())
                    .completed(isCompleted)
                    .isReward(isRewarded)
                    .build();
            })
            .collect(Collectors.toList());

    }

    public List<QuestAchievementDTO> getQuestAchievementsForToday(Long memberId) {
        Date today = new Date(System.currentTimeMillis());
        
        List<QuestAchievement> achievements = questAchievementRepository.findByMemberIdAndRewardDate(memberId, today);

        return achievements.stream()
                .map(achievement -> QuestAchievementDTO.builder()
                        .memberId(achievement.getMember().getId())
                        .questId(achievement.getQuest().getId())
                        .isRewarded(achievement.getIsRewarded())
                        .rewardDate(achievement.getRewardDate())
                        .build())
                .collect(Collectors.toList());
    }
    
    

    @Transactional
    public void rewardPoints(Long memberId, Long questId) {

        QuestAchievement.QuestAchievementId achievementId = new QuestAchievement.QuestAchievementId();
        achievementId.setMemberid(memberId);
        achievementId.setQuestid(questId);

        Optional<QuestAchievement> achievementOpt = questAchievementRepository.findById(achievementId);

        if (achievementOpt.isPresent()) {
            QuestAchievement achievement = achievementOpt.get();

            if (achievement.getRewardDate() == null && !achievement.getIsRewarded()) {
                
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
    	questAchievementRepository.deleteByRewardDateIsNull();
    }

}
