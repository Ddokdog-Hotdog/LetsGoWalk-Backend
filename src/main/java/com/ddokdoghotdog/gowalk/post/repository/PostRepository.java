package com.ddokdoghotdog.gowalk.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Optional<Post> findById(Long id);
	Optional<List<Post>> findByBoardId(Long boardId);
	
}
