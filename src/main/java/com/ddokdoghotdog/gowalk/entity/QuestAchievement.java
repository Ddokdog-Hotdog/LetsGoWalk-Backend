package com.ddokdoghotdog.gowalk.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "questachievements")
public class QuestAchievement {

    @EmbeddedId
    private QuestAchievementId id;

    @ManyToOne
    @MapsId("memberid")
    @JoinColumn(name = "memberid", nullable = false)
    private Member member;

    @ManyToOne
    @MapsId("questid")
    @JoinColumn(name = "questid", nullable = false)
    private Quest quest;

    @Column(name = "is_rewarded", nullable = false)
    private Boolean isRewarded;

    @Column(name = "reward_date")
    private Date rewardDate;

    @Embeddable
    public static class QuestAchievementId {
        private Long memberid;
        private Long questid;
    }
}
