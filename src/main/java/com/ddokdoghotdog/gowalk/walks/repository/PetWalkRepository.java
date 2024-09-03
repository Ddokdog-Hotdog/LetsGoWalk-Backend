package com.ddokdoghotdog.gowalk.walks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.PetWalk;

@Repository
public interface PetWalkRepository extends JpaRepository<PetWalk, Long> {

}