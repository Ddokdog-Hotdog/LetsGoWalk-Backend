package com.ddokdoghotdog.gowalk.quests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestDTO {
    private Long questId;
    private String questName;
    private String description;
    private int questPoint;
    private boolean completed;
    private boolean isReward;
}