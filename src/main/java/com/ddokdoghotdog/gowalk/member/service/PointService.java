package com.ddokdoghotdog.gowalk.member.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.entity.Order;
import com.ddokdoghotdog.gowalk.entity.Quest;
import com.ddokdoghotdog.gowalk.entity.QuestAchievement;
import com.ddokdoghotdog.gowalk.member.dto.PointDTO;
import com.ddokdoghotdog.gowalk.payment.repository.OrdersRepository;
import com.ddokdoghotdog.gowalk.payment.repository.PaymentRepository;
import com.ddokdoghotdog.gowalk.quests.repository.QuestAchievementRepository;
import com.ddokdoghotdog.gowalk.quests.repository.QuestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PointService {
    @Autowired
    private QuestAchievementRepository questAchievementRepository;
    @Autowired
    private QuestRepository questRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    public List<PointDTO> getPointTransactions(Long memberId) {
        List<PointDTO> transactions = new ArrayList<>();

        // 퀘스트 포인트 획득 정보 가져오기
        List<QuestAchievement> achievements = questAchievementRepository.findByIsRewardedTrueAndMemberId(memberId);
        System.out.println(achievements);
        for (QuestAchievement achievement : achievements) {
            Quest quest = questRepository.findById(achievement.getQuest().getId()).orElse(null);
            if (quest != null) {
                Date dateTime = achievement.getRewardDate();
                transactions.add(new PointDTO(quest.getName(), quest.getAchievementPoints(), dateTime, "plus"));
            }
        }

        List<Order> orders = ordersRepository.findByMemberId(memberId);
        // 결제 포인트 사용 정보 가져오기
        List<Long> orderIds = ordersRepository.findByMemberId(memberId).stream()
                                             .map(Order::getId)
                                             .collect(Collectors.toList());
        paymentRepository.findByOrderIdInAndPointAmountNot(orderIds,0).forEach(p -> {
            transactions.add(new PointDTO("Order Payment", -p.getPointAmount(), p.getPaidAt(), "minus"));
        });
        
        List<PointDTO> sortedTransactions = transactions.stream()
                .sorted(Comparator.comparing(PointDTO::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        return sortedTransactions;
    }
}

