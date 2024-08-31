package com.ddokdoghotdog.gowalk.walks.repository;

import java.util.List;
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

    @Query("SELECT DISTINCT w FROM Walk w " +
            "JOIN w.petWalks pw " +
            "JOIN pw.pet p " +
            "JOIN p.member m " +
            "WHERE w.id = :walkId AND m.id = :memberId")
    Optional<Walk> findByIdMemberId(@Param("walkId") Long walkId, @Param("memberId") Long memberId);

    @Query("SELECT DISTINCT w FROM Walk w " +
            "JOIN w.petWalks pw " +
            "JOIN pw.pet p " +
            "JOIN p.member m " +
            "WHERE m.id = :memberId " +
            "AND YEAR(w.starttime) = :year " +
            "AND MONTH(w.starttime) = :month")
    List<Walk> findAllByPetOwnerMemberIdAndMonth(@Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month);;
}
