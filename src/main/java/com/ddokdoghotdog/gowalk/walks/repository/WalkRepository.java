package com.ddokdoghotdog.gowalk.walks.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Walk;

@Repository
public interface WalkRepository extends JpaRepository<Walk, Long> {

    @Query("SELECT w FROM Walk w LEFT JOIN FETCH w.petWalks WHERE w.id = :walkId")
    Optional<Walk> findWalkWithPetsById(@Param("walkId") Long walkId);
}
