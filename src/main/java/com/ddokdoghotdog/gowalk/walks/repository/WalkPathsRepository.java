package com.ddokdoghotdog.gowalk.walks.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;

public interface WalkPathsRepository extends MongoRepository<WalkPaths, String> {

    List<WalkPaths> findAllByWalkIdIn(List<Long> walkIds);

    @Query(value = "{ 'paths.location': { $near: { $geometry: ?0, $maxDistance: ?1 } }, 'paths.recordTime': { $gte: ?2 }}")
    List<WalkPaths> findByLocationNear(GeoJsonPoint location, int maxDistance, Timestamp fromDate);
}
