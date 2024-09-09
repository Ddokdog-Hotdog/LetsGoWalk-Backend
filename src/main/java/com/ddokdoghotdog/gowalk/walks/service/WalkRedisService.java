package com.ddokdoghotdog.gowalk.walks.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Set;
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
    private final String WALK_KEY = "walk";
    private final String ROUTE_KEY = "route";
    private final String ROUTE_COUNT_KEY = "routeCount";
    private final String DISTANCE_KEY = "distance";

    public void initPath(Long walkId, PathPoint initialLocation) throws JsonProcessingException {
        String key = walkBaseKey(walkId) + "1";
        String locationJson = objectMapper.writeValueAsString(List.of(initialLocation));
        redisTemplate.opsForHash().put(key, "1", locationJson);
        redisTemplate.opsForHash().put(key, ROUTE_COUNT_KEY, "1");
        redisTemplate.expire(key, TTL, TimeUnit.HOURS);
    }

    public void updateWalkPath(Long walkId, List<PathPoint> newPoints) throws JsonProcessingException {
        String baseKey = walkBaseKey(walkId);

        // 현재 route 번호 가져오기
        String countStr = (String) redisTemplate.opsForHash().get(baseKey, ROUTE_COUNT_KEY);
        int count = (countStr == null) ? 1 : Integer.parseInt(countStr);

        // 새로운 경로 포인트 저장
        String pointsJson = objectMapper.writeValueAsString(newPoints);
        redisTemplate.opsForHash().put(baseKey, String.valueOf(count), pointsJson);
        redisTemplate.opsForHash().put(baseKey, ROUTE_COUNT_KEY, String.valueOf(count + 1));
        redisTemplate.expire(baseKey, TTL, TimeUnit.HOURS);
    }

    public List<PathPoint> getAllPathPoints(Long walkId) throws JsonProcessingException {
        List<PathPoint> allPoints = new ArrayList<>();
        String key = walkBaseKey(walkId);

        String countStr = (String) redisTemplate.opsForHash().get(key, ROUTE_COUNT_KEY);
        int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

        for (int i = 1; i <= count; i++) {
            String pointsJson = (String) redisTemplate.opsForHash().get(key, String.valueOf(i));
            if (pointsJson != null) {
                List<PathPoint> points = objectMapper.readValue(pointsJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, PathPoint.class));
                allPoints.addAll(points);
            }
        }

        return allPoints;
    }

    public void cleanupRedisData(Long walkId) {
        String key = walkBaseKey(walkId);
        redisTemplate.delete(key);
    }

    private String walkBaseKey(Long walkId) {
        return WALK_KEY + ":" + walkId + ":" + ROUTE_KEY;
    }

    public void updateDistance(Long walkId, double distance) {
        String key = walkBaseKey(walkId);
        redisTemplate.opsForHash().put(key, DISTANCE_KEY, String.valueOf(distance));
    }

    public Double getDistance(Long walkId) {
        String key = walkBaseKey(walkId);
        String distanceStr = (String) redisTemplate.opsForHash().get(key, DISTANCE_KEY);
        return distanceStr != null ? Double.valueOf(distanceStr) : null;
    }

    public Set<String> getAllActiveWalkIds() {
        return redisTemplate.keys(WALK_KEY + ":*:" + ROUTE_KEY);
    }
}
