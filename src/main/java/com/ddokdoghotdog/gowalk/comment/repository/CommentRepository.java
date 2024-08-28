package com.ddokdoghotdog.gowalk.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
