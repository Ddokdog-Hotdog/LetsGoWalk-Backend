package com.ddokdoghotdog.gowalk.global.jwt;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
    // JpaRepository와 CrudRepository의 차이
    // 페이징 및 정렬기능이 없음 -> 필요한 기능을 상속
    Optional<Token> findByAccessToken(String accessToken);
}
