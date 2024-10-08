package com.ddokdoghotdog.gowalk.pet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ddokdoghotdog.gowalk.entity.Breed;
import com.ddokdoghotdog.gowalk.global.annotation.RequiredMemberId;
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

    @RequiredMemberId
    @GetMapping("")
    public ResponseEntity<List<PetDTO.Response>> getPetsByMember(Long memberId) {
        return new ResponseEntity<>(petReadService.getPetsByMemberId(memberId), HttpStatus.OK);
    }
/*
    @GetMapping("/breeds")
    public ResponseEntity<List<Breed>> getBreeds() {
        return new ResponseEntity<>(petReadService.getBreedList(), HttpStatus.OK);
    }*/
    
    @GetMapping("/breeds")
    public ResponseEntity<List<Breed>> getBreeds(@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return new ResponseEntity<>(petReadService.getBreedListFiltered(search), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(petReadService.getBreedList(), HttpStatus.OK);
        }
    }
    
    @GetMapping("/breeds/{breedId}")
    public ResponseEntity<Breed> getBreedById(@PathVariable("breedId") Long breedId) {
        Breed breed = petReadService.getBreedById(breedId);
        if (breed != null) {
            return new ResponseEntity<>(breed, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profile/{petId}")
    public ResponseEntity<PetDTO.Response> getPetById(@PathVariable("petId") Long petId) {
        return new ResponseEntity<>(petReadService.getPetById(petId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PetDTO.Response> createPet(@RequestPart PetDTO.CreateRequest petCreateRequestDTO, 
    		@RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
    	PetDTO.Response response = petWriteService.createPet(petCreateRequestDTO, profileImage);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<PetDTO.Response> updatePet(@RequestPart("petUpdateRequestDTO") PetDTO.Update petUpdateRequestDTO,
    	    @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
    	System.out.println(petUpdateRequestDTO.getPetId());
    	PetDTO.Response response = petWriteService.updatePet(petUpdateRequestDTO, profileImage);
    	    
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequiredMemberId
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("petId") Long petId, Long memberId) {
        petWriteService.deletePet(petId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
