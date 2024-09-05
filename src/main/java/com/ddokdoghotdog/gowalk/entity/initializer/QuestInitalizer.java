package com.ddokdoghotdog.gowalk.entity.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Quest;
import com.ddokdoghotdog.gowalk.quests.repository.QuestRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Profile("dev")
public class QuestInitalizer {
    private final QuestRepository questRepository;

    @Bean
    @Transactional
    public CommandLineRunner initalizeQuests() {

        return args -> {
            if (questRepository.findByName("출석체크").isEmpty()) {
                questRepository.save(Quest.builder()
                        .achievementPoints(10L)
                        .description("출석체크하고 10P 받기")
                        .isVisible(true)
                        .name("출석체크")
                        .build());
            }

            if (questRepository.findByName("산책").isEmpty()) {
                questRepository.save(Quest.builder()
                        .achievementPoints(50L)
                        .description("산책하고 50p 받기")
                        .isVisible(true)
                        .name("산책")
                        .build());
            }

            if (questRepository.findByName("게시물 업로드").isEmpty()) {
                questRepository.save(Quest.builder()
                        .achievementPoints(20L)
                        .description("게시물 작성하고 20p 받기")
                        .isVisible(true)
                        .name("게시물 업로드")
                        .build());
            }

            if (questRepository.findByName("전문가 상담").isEmpty()) {
                questRepository.save(Quest.builder()
                        .achievementPoints(30L)
                        .description("전문가에게 문의하고 30p 받기")
                        .isVisible(false)
                        .name("전문가 상담")
                        .build());
            }
        };
    }
}
