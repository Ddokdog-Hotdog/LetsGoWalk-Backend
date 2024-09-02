package com.ddokdoghotdog.gowalk.quests.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestAchievementDTO {

    private Long memberId;
    private Long questId;
    private Boolean isRewarded;
    private Date rewardDate;
}
