package com.ddokdoghotdog.gowalk.pet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.auth.service.MemberService;
import com.ddokdoghotdog.gowalk.pet.Service.PetReadService;
import com.ddokdoghotdog.gowalk.pet.Service.PetWriteService;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mypage/pets")
public class PetController {
    private final PetReadService petReadService;
    private final PetWriteService petWriteService;
    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<PetDTO.Response> createPet(@RequestBody PetDTO.CreateRequest petDTO) {
        return new ResponseEntity<>(petWriteService.createPet(petDTO), HttpStatus.CREATED);
    }
}
