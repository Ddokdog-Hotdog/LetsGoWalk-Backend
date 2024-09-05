package com.ddokdoghotdog.gowalk.pet.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.member.service.MemberService;
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

    @GetMapping("")
    public ResponseEntity<List<PetDTO.Response>> getPetsByMember(Principal principal) {
        String name = principal.getName();
        Long memberId = Long.parseLong(name);
        return new ResponseEntity<>(petReadService.getPetsByMemberId(memberId), HttpStatus.OK);
    }

    @GetMapping("/profile/{petId}")
    public ResponseEntity<PetDTO.Response> getPetById(@PathVariable("petId") Long petId) {
        return new ResponseEntity<>(petReadService.getPetById(petId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PetDTO.Response> createPet(@RequestBody PetDTO.CreateRequest petCreateRequestDTO) {
        return new ResponseEntity<>(petWriteService.createPet(petCreateRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<PetDTO.Response> updatePet(@RequestBody PetDTO.Update petUpdateRequsetDTO) {
        return new ResponseEntity<>(petWriteService.updatePet(petUpdateRequsetDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("petId") Long petId, Principal principal) {
        String name = principal.getName();
        Long memberId = Long.parseLong(name);
        petWriteService.deletePet(petId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
