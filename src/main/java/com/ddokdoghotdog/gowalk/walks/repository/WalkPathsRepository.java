package com.ddokdoghotdog.gowalk.walks.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;

public interface WalkPathsRepository extends MongoRepository<WalkPaths, String> {

    List<WalkPaths> findAllByWalkIdIn(List<Long> walkIds);
}
