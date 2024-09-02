package com.ddokdoghotdog.gowalk.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Override
    Optional<Pet> findById(Long id);

    @Query("SELECT p FROM Pet p JOIN FETCH p.breed WHERE p.member.id = :memberId AND p.id = :id")
    Optional<Pet> findByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);

    List<Pet> findByMember(Member member);

    @Query("SELECT p FROM Pet p JOIN FETCH p.breed WHERE p.member.id = :memberId")
    List<Pet> findByMemberId(Long memberId);

    @Query("SELECT p FROM Pet p JOIN FETCH p.breed WHERE p.id IN :ids AND p.member.id = :memberId")
    List<Pet> findAllByIdInAndMemberId(@Param("ids") List<Long> ids, @Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Pet p WHERE p.id = :id AND p.member.id = :memberId")
    void deleteByPetIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);
}
