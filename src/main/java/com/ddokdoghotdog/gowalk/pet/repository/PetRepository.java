package com.ddokdoghotdog.gowalk.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Override
    Optional<Pet> findById(Long id);

    List<Pet> findByMember(Member member);

    List<Pet> findByMemberId(Long memberId);
}
