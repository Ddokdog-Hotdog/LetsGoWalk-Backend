package com.ddokdoghotdog.gowalk.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.MediaUrlList;

@Repository
public interface MediaUrlRepository extends JpaRepository<MediaUrlList, Long> {
	
	@Transactional
    @Modifying
    @Query("DELETE FROM MediaUrlList m WHERE m.post.id = ?1")
	int deleteByPostId(Long postId);
}
