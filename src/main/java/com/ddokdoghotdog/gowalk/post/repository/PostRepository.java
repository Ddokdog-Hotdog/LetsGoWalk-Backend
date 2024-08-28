package com.ddokdoghotdog.gowalk.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
}
