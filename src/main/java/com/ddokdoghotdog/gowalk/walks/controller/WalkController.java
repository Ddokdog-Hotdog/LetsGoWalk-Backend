package com.ddokdoghotdog.gowalk.walks.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO;
import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.service.WalkReadService;
import com.ddokdoghotdog.gowalk.walks.service.WalkWriteService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/walks")
public class WalkController {

    private final WalkWriteService walkWriteService;
    private final WalkReadService walkReadService;

    @PostMapping("/start")
    public ResponseEntity<WalkDTO.WalkStartResponse> startWalk(@RequestBody WalkDTO.WalkStartRequest walkStartDTO)
            throws JsonProcessingException {
        return new ResponseEntity<>(walkWriteService.startWalk(walkStartDTO), HttpStatus.CREATED);
    }

    @PostMapping("/end")
    public ResponseEntity<WalkDTO.WalkEndResponse> endWalk(@RequestBody WalkDTO.WalkEndRequest walkEndDTO) {
        return null;
    }

    @PostMapping
    public ResponseEntity<WalkPaths> createWalk(@RequestBody WalkPaths walk) {
        System.out.println("Received walk: " + walk); // 로그 추가
        WalkPaths savedWalk = walkWriteService.saveWalk(walk);
        System.out.println("Saved walk: " + savedWalk); // 로그 추가
        return new ResponseEntity<>(savedWalk, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WalkPaths>> getAllWalks() {
        List<WalkPaths> walks = walkReadService.getAllWalks();
        return new ResponseEntity<>(walks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalkPaths> getWalkById(@PathVariable String id) {
        return walkReadService.getWalkById(id)
                .map(walk -> new ResponseEntity<>(walk, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
