package com.ddokdoghotdog.gowalk.quests.service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Quest;
import com.ddokdoghotdog.gowalk.entity.QuestAchievement;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
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
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUEST_NOT_FOUND));
        
        List<QuestAchievement> achievementQuest = questAchievementRepository.findByMemberIdAndQuestId(memberId, questId);

        long currentTimeMillis = System.currentTimeMillis();
        Date todaySqlDate = new Date(currentTimeMillis);
        LocalDate todayLocalDate = todaySqlDate.toLocalDate(); 
 
        if (achievementQuest.isEmpty()) {
        	QuestAchievement realachievement = QuestAchievement.builder()
                    .member(member)
                    .quest(quest)
                    .isRewarded(false)
                    .rewardDate(null)
                    .build();

            questAchievementRepository.save(realachievement);
	    }else {
	    	for (QuestAchievement achievement : achievementQuest) {
	    		
	    		Date rewardDate =achievement.getRewardDate();
	    		System.out.println(rewardDate);
	    		System.out.println(todaySqlDate);
	            if (achievement.getRewardDate() == null) {
	            	System.out.println(1);
	            	break;
	            }
	            LocalDate rewardLocalDate = rewardDate.toLocalDate(); // sql.Date를 LocalDate로 변환
	            if (todayLocalDate.equals(rewardLocalDate)) {
	            	System.out.println(2);
	            	break;
	            }
	            else{
	            	System.out.println(3);
	            	QuestAchievement realachievement = QuestAchievement.builder()
	                        .member(member)
	                        .quest(quest)
	                        .isRewarded(false)
	                        .rewardDate(null)
	                        .build();
	
	                questAchievementRepository.save(realachievement);
	                break;
	            }
	        }
	
        }
        
    }
    
    public List<Quest> getQuestList() {
        return questRepository.findByIsVisibleTrue();
    }
 
    public List<QuestDTO> getVisibleQuestsAndAchievementsForToday(Long memberId) {
    	long currentTimeMillis = System.currentTimeMillis();
        Date todaySqlDate = new Date(currentTimeMillis);
        LocalDate today = todaySqlDate.toLocalDate(); 

        List<Quest> visibleQuests = questRepository.findByIsVisibleTrue();
        List<QuestAchievement> achievements = questAchievementRepository.findByMemberId(memberId);

        return visibleQuests.stream()
                .map(quest -> {
                    List<QuestAchievement> questAchievements = achievements.stream()
                        .filter(a -> a.getQuest().getId().equals(quest.getId()))
                        .collect(Collectors.toList());

                    boolean isCompleted = !questAchievements.isEmpty();
                    boolean isRewarded = questAchievements.stream()
                        .anyMatch(a -> a.getRewardDate() != null && today.equals(a.getRewardDate().toLocalDate()));

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
    	long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        
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
        
        List<QuestAchievement> achievements = questAchievementRepository.findByMemberIdAndQuestIdAndIsRewardedFalse(memberId, questId);

        for (QuestAchievement achievement : achievements) {
            Member member = achievement.getMember();
            Quest quest = achievement.getQuest();

            member.setPoint(member.getPoint() + quest.getAchievementPoints());

            achievement.setIsRewarded(true);
            achievement.setRewardDate(new Date(System.currentTimeMillis()));

            memberRepository.save(member);
            questAchievementRepository.save(achievement);
        }
    }
    
    

    
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteUnrewardedAchievements() {
    	questAchievementRepository.deleteByRewardDateIsNull();
    }

}
