package com.ddokdoghotdog.gowalk.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Comment;
import com.ddokdoghotdog.gowalk.entity.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("SELECT p FROM Post p WHERE p.id = :postId AND p.member.id = :memberId")
    Optional<Post> findByIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);
}
