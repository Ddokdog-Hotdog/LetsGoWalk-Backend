package com.ddokdoghotdog.gowalk.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ddokdoghotdog.gowalk.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Optional<Post> findByIdAndMemberId(@Param("id") Long id, @Param("memberid") Long memberId);
	
	Page<Post> findByBoardId(Long boardid, Pageable pageable);	
}
