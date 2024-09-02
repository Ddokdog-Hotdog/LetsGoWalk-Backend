package com.ddokdoghotdog.gowalk.walks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO;
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

    @PostMapping("/update")
    public ResponseEntity<WalkDTO.WalkUpdateResponse> updateWalk(@RequestBody WalkDTO.WalkUpdateRequest walkUpdateDTO)
            throws JsonProcessingException {
        return new ResponseEntity<>(walkWriteService.recordWalkPath(walkUpdateDTO), HttpStatus.OK);
    }

    @PostMapping("/end")
    public ResponseEntity<WalkDTO.WalkEndResponse> endWalk(@RequestBody WalkDTO.WalkEndRequest walkEndDTO)
            throws JsonProcessingException {
        return new ResponseEntity<>(walkWriteService.endWalk(walkEndDTO), HttpStatus.OK);
    }

    @PostMapping("/daily")
    public ResponseEntity<WalkSummaryDTO.DailyWalkSummaryResponse> getDailyWalks(
            @RequestBody WalkSummaryDTO.DailyWalkSummaryRequest walkSummaryDTO) {
        return new ResponseEntity<>(walkReadService.getDailyWalk(walkSummaryDTO), HttpStatus.OK);
    }

    @PostMapping("/monthly")
    public ResponseEntity<WalkSummaryDTO.MonthlyWalkSummaryResponse> getMonthlyWalks(
            @RequestBody WalkSummaryDTO.MonthlyWalkSummaryRequest walkSummaryDTO) {
        return new ResponseEntity<>(walkReadService.getMonthlyWalk(walkSummaryDTO), HttpStatus.OK);
    }
}
