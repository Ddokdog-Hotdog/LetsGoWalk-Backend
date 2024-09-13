package com.ddokdoghotdog.gowalk.quests.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.QuestAchievement;

@Repository
public interface QuestAchievementRepository extends JpaRepository<QuestAchievement, Long> {
    
	List<QuestAchievement> findByMemberIdAndRewardDate(Long memberId, java.util.Date today);
	
	void deleteByRewardDateIsNull();

	List<QuestAchievement> findByMemberId(Long memberId);

	List<QuestAchievement> findByMemberIdAndQuestId(Long memberId, Long questId);
	
	List<QuestAchievement> findByIsRewardedTrue();
	
	List<QuestAchievement> findByIsRewardedTrueAndMemberId(Long memberId);

	List<QuestAchievement> findByMemberIdAndQuestIdAndIsRewardedFalse(Long memberId, Long questId);
	
}