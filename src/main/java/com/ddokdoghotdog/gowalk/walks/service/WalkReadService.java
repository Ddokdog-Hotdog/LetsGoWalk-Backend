package com.ddokdoghotdog.gowalk.walks.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.repository.WalkPathsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WalkReadService {
    private final WalkPathsRepository repository;

    public List<WalkPaths> getAllWalks() {
        return repository.findAll();
    }

    public Optional<WalkPaths> getWalkById(String id) {
        return repository.findById(id);
    }

}
