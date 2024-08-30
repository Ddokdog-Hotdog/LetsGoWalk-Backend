package com.ddokdoghotdog.gowalk.quests.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.QuestAchievement;
import com.ddokdoghotdog.gowalk.entity.QuestAchievement.QuestAchievementId;

@Repository
public interface QuestAchievementRepository extends JpaRepository<QuestAchievement, QuestAchievementId> {
    List<QuestAchievement> findByMemberIdAndRewardDate(Long memberId, Date rewardDate);
}