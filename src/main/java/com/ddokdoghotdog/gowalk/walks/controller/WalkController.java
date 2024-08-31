package com.ddokdoghotdog.gowalk.walks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO;
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
}
