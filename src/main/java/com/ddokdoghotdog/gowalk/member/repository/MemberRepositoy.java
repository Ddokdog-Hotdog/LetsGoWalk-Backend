package com.ddokdoghotdog.gowalk.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddokdoghotdog.gowalk.entity.UserRole;

public interface MemberRepositoy extends JpaRepository<UserRole, Long> {

}
