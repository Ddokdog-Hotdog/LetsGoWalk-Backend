// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.stereotype.Service;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.util.*;

// @Service
// public class WalkService {
// private final RedisTemplate<String, String> redisTemplate;
// private final MongoTemplate mongoTemplate;
// private final ObjectMapper objectMapper;
// private final WalkRepository walkRepository;

// public WalkService(RedisTemplate<String, String> redisTemplate,
// MongoTemplate mongoTemplate,
// ObjectMapper objectMapper,
// WalkRepository walkRepository) {
// this.redisTemplate = redisTemplate;
// this.mongoTemplate = mongoTemplate;
// this.objectMapper = objectMapper;
// this.walkRepository = walkRepository;
// }

// public void startWalk(Long walkId, PathPoint initialLocation) {
// // Oracle DB에 Walk 엔티티 저장
// Walk walk = new Walk();
// walk.setId(walkId);
// walk.setStartTime(new Date());
// walkRepository.save(walk);

// // 초기 위치를 Redis에 저장
// String key = "walk:" + walkId + ":route1";
// try {
// String locationJson =
// objectMapper.writeValueAsString(List.of(initialLocation));
// redisTemplate.opsForValue().set(key, locationJson);
// } catch (Exception e) {
// // 예외 처리
// }
// }

// public void updateWalkPath(Long walkId, List<PathPoint> newPoints) {
// String baseKey = "walk:" + walkId + ":route";
// String countKey = "walk:" + walkId + ":routeCount";

// // 현재 route 번호 가져오기
// String countStr = redisTemplate.opsForValue().get(countKey);
// int count = (countStr == null) ? 1 : Integer.parseInt(countStr);

// // 새로운 경로 포인트 저장
// String key = baseKey + count;
// try {
// String pointsJson = objectMapper.writeValueAsString(newPoints);
// redisTemplate.opsForValue().set(key, pointsJson);
// } catch (Exception e) {
// // 예외 처리
// }

// // route 번호 증가
// redisTemplate.opsForValue().set(countKey, String.valueOf(count + 1));
// }

// public void endWalk(Long walkId, PathPoint finalLocation) {
// // 모든 경로 포인트 가져오기
// List<PathPoint> allPoints = getAllPathPoints(walkId);
// allPoints.add(finalLocation);

// // 시간순 정렬
// allPoints.sort(Comparator.comparing(PathPoint::getRecordTime));

// // MongoDB에 저장
// WalkPaths walkPaths = new WalkPaths();
// walkPaths.setWalkId(walkId);
// walkPaths.setPaths(allPoints);
// mongoTemplate.save(walkPaths);

// // Oracle DB 업데이트
// Walk walk = walkRepository.findById(walkId).orElseThrow();
// walk.setEndTime(new Date());
// // totalDistance와 totalCalories 계산 로직 추가 필요
// walkRepository.save(walk);

// // Redis에서 데이터 삭제
// cleanupRedisData(walkId);
// }

// private List<PathPoint> getAllPathPoints(Long walkId) {
// List<PathPoint> allPoints = new ArrayList<>();
// String baseKey = "walk:" + walkId + ":route";
// String countKey = "walk:" + walkId + ":routeCount";

// String countStr = redisTemplate.opsForValue().get(countKey);
// int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

// for (int i = 1; i <= count; i++) {
// String key = baseKey + i;
// String pointsJson = redisTemplate.opsForValue().get(key);
// if (pointsJson != null) {
// try {
// List<PathPoint> points = objectMapper.readValue(pointsJson,
// objectMapper.getTypeFactory().constructCollectionType(List.class,
// PathPoint.class));
// allPoints.addAll(points);
// } catch (Exception e) {
// // 예외 처리
// }
// }
// }

// return allPoints;
// }

// private void cleanupRedisData(Long walkId) {
// String baseKey = "walk:" + walkId + ":route";
// String countKey = "walk:" + walkId + ":routeCount";

// String countStr = redisTemplate.opsForValue().get(countKey);
// int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

// for (int i = 1; i <= count; i++) {
// String key = baseKey + i;
// redisTemplate.delete(key);
// }
// redisTemplate.delete(countKey);
// }
// }// walk.setId(walkId);
// // walk.setStartTime(new Date());
// // walkRepository.save(walk);

// // // 초기 위치를 Redis에 저장
// // String key = "walk:" + walkId + ":route1";
// // try {
// // String locationJson =
// objectMapper.writeValueAsString(List.of(initialLocation));
// // redisTemplate.opsForValue().set(key, locationJson);
// // } catch (Exception e) {
// // // 예외 처리
// // }
// // }

// // public void updateWalkPath(Long walkId, List<PathPoint> newPoints) {
// // String baseKey = "walk:" + walkId + ":route";
// // String countKey = "walk:" + walkId + ":routeCount";

// // // 현재 route 번호 가져오기
// // String countStr = redisTemplate.opsForValue().get(countKey);
// // int count = (countStr == null) ? 1 : Integer.parseInt(countStr);

// // // 새로운 경로 포인트 저장
// // String key = baseKey + count;
// // try {
// // String pointsJson = objectMapper.writeValueAsString(newPoints);
// // redisTemplate.opsForValue().set(key, pointsJson);
// // } catch (Exception e) {
// // // 예외 처리
// // }

// // // route 번호 증가
// // redisTemplate.opsForValue().set(countKey, String.valueOf(count + 1));
// // }

// // public void endWalk(Long walkId, PathPoint finalLocation) {
// // // 모든 경로 포인트 가져오기
// // List<PathPoint> allPoints = getAllPathPoints(walkId);
// // allPoints.add(finalLocation);

// // // 시간순 정렬
// // allPoints.sort(Comparator.comparing(PathPoint::getRecordTime));

// // // MongoDB에 저장
// // WalkPaths walkPaths = new WalkPaths();
// // walkPaths.setWalkId(walkId);
// // walkPaths.setPaths(allPoints);
// // mongoTemplate.save(walkPaths);

// // // Oracle DB 업데이트
// // Walk walk = walkRepository.findById(walkId).orElseThrow();
// // walk.setEndTime(new Date());
// // // totalDistance와 totalCalories 계산 로직 추가 필요
// // walkRepository.save(walk);

// // // Redis에서 데이터 삭제
// // cleanupRedisData(walkId);
// // }

// // private List<PathPoint> getAllPathPoints(Long walkId) {
// // List<PathPoint> allPoints = new ArrayList<>();
// // String baseKey = "walk:" + walkId + ":route";
// // String countKey = "walk:" + walkId + ":routeCount";

// // String countStr = redisTemplate.opsForValue().get(countKey);
// // int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

// // for (int i = 1; i <= count; i++) {
// // String key = baseKey + i;
// // String pointsJson = redisTemplate.opsForValue().get(key);
// // if (pointsJson != null) {
// // try {
// // List<PathPoint> points = objectMapper.readValue(pointsJson,
// // objectMapper.getTypeFactory().constructCollectionType(List.class,
// PathPoint.class));
// // allPoints.addAll(points);
// // } catch (Exception e) {
// // // 예외 처리
// // }
// // }
// // }

// // return allPoints;
// // }

// // private void cleanupRedisData(Long walkId) {
// // String baseKey = "walk:" + walkId + ":route";
// // String countKey = "walk:" + walkId + ":routeCount";

// // String countStr = redisTemplate.opsForValue().get(countKey);
// // int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

// // for (int i = 1; i <= count; i++) {
// // String key = baseKey + i;
// // redisTemplate.delete(key);
// // }
// // redisTemplate.delete(countKey);
// // }
// // }