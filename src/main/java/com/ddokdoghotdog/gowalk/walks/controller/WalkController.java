package com.ddokdoghotdog.gowalk.walks.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO.WalkStartResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO.WalkEndRequest;
import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO.WalkEndResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO.WalkStartRequest;
import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO.WalkUpdateRequest;
import com.ddokdoghotdog.gowalk.walks.dto.WalkDTO.WalkUpdateResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.DailyWalkSummaryRequest;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.DailyWalkSummaryResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.MonthlyWalkSummaryRequest;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.MonthlyWalkSummaryResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.NearbyHotplaceResponse;
import com.ddokdoghotdog.gowalk.walks.dto.WalkSummaryDTO.NearbyWalkPathsRequest;
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
    public ResponseEntity<WalkStartResponse> startWalk(@RequestBody WalkStartRequest walkStartDTO)
            throws JsonProcessingException {
        return new ResponseEntity<>(walkWriteService.startWalk(walkStartDTO), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<WalkUpdateResponse> updateWalk(@RequestBody WalkUpdateRequest walkUpdateDTO)
            throws JsonProcessingException {
        return new ResponseEntity<>(walkWriteService.recordWalkPath(walkUpdateDTO), HttpStatus.OK);
    }

    @PostMapping("/end")
    public ResponseEntity<WalkEndResponse> endWalk(@RequestBody WalkEndRequest walkEndDTO)
            throws JsonProcessingException {
        return new ResponseEntity<>(walkWriteService.endWalk(walkEndDTO), HttpStatus.OK);
    }

    @PostMapping("/daily")
    public ResponseEntity<DailyWalkSummaryResponse> getDailyWalks(
            @RequestBody DailyWalkSummaryRequest walkSummaryDTO) {
        return new ResponseEntity<>(walkReadService.getDailyWalk(walkSummaryDTO), HttpStatus.OK);
    }

    @PostMapping("/monthly")
    public ResponseEntity<MonthlyWalkSummaryResponse> getMonthlyWalks(
            @RequestBody MonthlyWalkSummaryRequest walkSummaryDTO) {
        return new ResponseEntity<>(walkReadService.getMonthlyWalk(walkSummaryDTO), HttpStatus.OK);
    }

    @PostMapping("/nearby")
    public ResponseEntity<List<WalkPaths>> getNearbyPaths(
            @RequestBody NearbyWalkPathsRequest NearWalkPathDTO) {
        return new ResponseEntity<>(walkReadService.getNearbyWalkPaths(NearWalkPathDTO), HttpStatus.OK);
    }

    @PostMapping("/hotplace")
    public ResponseEntity<List<NearbyHotplaceResponse>> getNearbyHotspots(
            @RequestBody NearbyWalkPathsRequest NearWalkPathDTO) {
        return new ResponseEntity<>(walkReadService.getNearbyHotspots(NearWalkPathDTO), HttpStatus.OK);
    }
}
