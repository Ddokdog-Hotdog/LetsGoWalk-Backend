package com.ddokdoghotdog.gowalk.walks.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.repository.WalkPathsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WalkService {

    private final WalkPathsRepository repository;

    public WalkPaths saveWalk(WalkPaths walk) {
        return repository.save(walk);
    }

    public List<WalkPaths> getAllWalks() {
        return repository.findAll();
    }

    public Optional<WalkPaths> getWalkById(String id) {
        return repository.findById(id);
    }

    public void deleteWalk(String id) {
        repository.deleteById(id);
    }
}
