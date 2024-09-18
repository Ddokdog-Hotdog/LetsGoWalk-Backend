package com.ddokdoghotdog.gowalk.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ddokdoghotdog.gowalk.entity.Breed;

public interface BreedRepository extends JpaRepository<Breed, Long> {
    Optional<Breed> findByName(String name);
    @Query("SELECT b FROM Breed b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Breed> findByNameContainingIgnoreCase(@Param("search") String search);
}
