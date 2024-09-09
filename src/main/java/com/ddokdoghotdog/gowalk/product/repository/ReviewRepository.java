package com.ddokdoghotdog.gowalk.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

}
