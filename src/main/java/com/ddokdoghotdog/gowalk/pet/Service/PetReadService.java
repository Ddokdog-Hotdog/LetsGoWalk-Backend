package com.ddokdoghotdog.gowalk.pet.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Breed;
import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO;
import com.ddokdoghotdog.gowalk.pet.repository.BreedRepository;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PetReadService {
    private final PetRepository petRepository;
    private final BreedRepository breedRepository;

    public PetDTO.Response getPetById(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PET_NOT_FOUND));
        return PetDTO.Response.of(pet);
    }

    public Pet getPetByIdAndMemberId(Long id, Long memberId) {
        return petRepository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PET_NOT_FOUND));
    }

    public List<PetDTO.Response> getPetsByMemberId(Long memberId) {
        List<Pet> pets = petRepository.findByMemberId(memberId);

        return pets.stream()
                .map(PetDTO.Response::of)
                .collect(Collectors.toList());
    }

    public List<Breed> getBreedList() {
        return breedRepository.findAll();
    }

    public Breed getBreedById(long id) {
        return breedRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.BREED_NOT_FOUND));
    }

    public Breed getBreedByName(String name) {
        return breedRepository.findByName(name).orElseThrow(() -> new BusinessException(ErrorCode.BREED_NOT_FOUND));
    }
}
