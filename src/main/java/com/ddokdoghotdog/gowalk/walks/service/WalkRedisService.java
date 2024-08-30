package com.ddokdoghotdog.gowalk.walks.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths.PathPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WalkRedisService {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final int TTL = 24;

    public void initPath(Long walkId, PathPoint initialLocation) throws JsonProcessingException {
        String key = "walk:" + walkId + ":route1";
        String locationJson = objectMapper.writeValueAsString(List.of(initialLocation));
        redisTemplate.opsForValue().set(key, locationJson, TTL, TimeUnit.HOURS);
    }

    public void updateWalkPath(Long walkId, List<PathPoint> newPoints) throws JsonProcessingException {
        String baseKey = "walk:" + walkId + ":route";
        String countKey = "walk:" + walkId + ":routeCount";

        // 현재 route 번호 가져오기
        String countStr = redisTemplate.opsForValue().get(countKey);
        int count = (countStr == null) ? 1 : Integer.parseInt(countStr);

        // 새로운 경로 포인트 저장
        String key = baseKey + count;
        String pointsJson = objectMapper.writeValueAsString(newPoints);
        redisTemplate.opsForValue().set(key, pointsJson, TTL, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(countKey, String.valueOf(count + 1), TTL, TimeUnit.HOURS);
    }

    public List<PathPoint> getAllPathPoints(Long walkId) throws JsonProcessingException {
        List<PathPoint> allPoints = new ArrayList<>();
        String baseKey = "walk:" + walkId + ":route";
        String countKey = "walk:" + walkId + ":routeCount";

        String countStr = redisTemplate.opsForValue().get(countKey);
        int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

        for (int i = 1; i <= count; i++) {
            String key = baseKey + i;
            String pointsJson = redisTemplate.opsForValue().get(key);
            if (pointsJson != null) {
                List<PathPoint> points = objectMapper.readValue(pointsJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, PathPoint.class));
                allPoints.addAll(points);
            }
        }

        return allPoints;
    }

    public void cleanupRedisData(Long walkId) {
        String baseKey = "walk:" + walkId + ":route";
        String countKey = "walk:" + walkId + ":routeCount";

        String countStr = redisTemplate.opsForValue().get(countKey);
        int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

        for (int i = 1; i <= count; i++) {
            String key = baseKey + i;
            redisTemplate.delete(key);
        }
        redisTemplate.delete(countKey);
    }
}
