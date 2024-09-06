package com.ddokdoghotdog.gowalk.quests.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.QuestAchievement;

@Repository
public interface QuestAchievementRepository extends JpaRepository<QuestAchievement, Long> {
    
	List<QuestAchievement> findByMemberIdAndRewardDate(Long memberId, Date rewardDate);
	
	void deleteByRewardDateIsNull();

	List<QuestAchievement> findByMemberId(Long memberId);

	Optional<QuestAchievement> findByMemberIdAndQuestId(Long memberId, Long questId);
	
	List<QuestAchievement> findByIsRewardedTrue();
	
	List<QuestAchievement> findByIsRewardedTrueAndMemberId(Long memberId);

	List<QuestAchievement> findByMemberIdAndQuestIdAndIsRewardedFalse(Long memberId, Long questId);
	
}