package com.ddokdoghotdog.gowalk.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddokdoghotdog.gowalk.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndSocialProvider(String email, String provider);

    Optional<Member> findByMemberKey(String memberKey);
}
