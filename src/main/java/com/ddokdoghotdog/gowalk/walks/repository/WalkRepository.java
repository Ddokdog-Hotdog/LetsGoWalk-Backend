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
                        "LEFT JOIN FETCH w.petWalks pw " +
                        "LEFT JOIN FETCH pw.pet p " +
                        "LEFT JOIN FETCH p.member m " +
                        "LEFT JOIN FETCH p.breed " +
                        "LEFT JOIN FETCH m.role " +
                        "WHERE w.id = :walkId AND m.id = :memberId AND w.endTime IS NOT NULL")
        Optional<Walk> findByIdAndMemberId(@Param("walkId") Long walkId,
                        @Param("memberId") Long memberId);

        @Query("SELECT DISTINCT w FROM Walk w " +
                        "LEFT JOIN FETCH w.petWalks pw " +
                        "LEFT JOIN FETCH pw.pet p " +
                        "LEFT JOIN FETCH p.member m " +
                        "LEFT JOIN FETCH p.breed " +
                        "LEFT JOIN FETCH m.role " +
                        "WHERE m.id = :memberId " +
                        "AND YEAR(w.startTime) = :year " +
                        "AND MONTH(w.startTime) = :month " +
                        "AND w.endTime IS NOT NULL")
        List<Walk> findAllByPetOwnerMemberIdAndMonth(@Param("memberId") Long memberId,
                        @Param("year") int year,
                        @Param("month") int month);;

        @Query("SELECT DISTINCT w FROM Walk w " +
                        "LEFT JOIN FETCH w.petWalks pw " +
                        "LEFT JOIN FETCH pw.pet p " +
                        "LEFT JOIN FETCH p.member m " +
                        "LEFT JOIN FETCH p.breed " +
                        "LEFT JOIN FETCH m.role " +
                        "WHERE m.id = :memberId " +
                        "AND YEAR(w.startTime) = :year " +
                        "AND MONTH(w.startTime) = :month " +
                        "AND DAY(w.startTime) = :day " +
                        "AND w.endTime IS NOT NULL")
        List<Walk> findAllByPetOwnerMemberIdAndDay(@Param("memberId") Long memberId,
                        @Param("year") int year,
                        @Param("month") int month,
                        @Param("day") int day);

        @Query("SELECT w.id FROM Walk w WHERE w.endTime IS NULL")
        List<Long> findAllUnfinishedWalkIds();
}
