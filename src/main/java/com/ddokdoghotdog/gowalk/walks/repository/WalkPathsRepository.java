package com.ddokdoghotdog.gowalk.walks.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;

public interface WalkPathsRepository extends MongoRepository<WalkPaths, String> {

}
