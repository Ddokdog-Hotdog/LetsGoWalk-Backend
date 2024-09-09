package com.ddokdoghotdog.gowalk.quests.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Quest;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {

    List<Quest> findByIsVisibleTrue();

    Optional<Quest> findByName(String name);
}
