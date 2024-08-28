package com.ddokdoghotdog.gowalk.pet.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Breed;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.pet.repository.BreedRepository;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PetReadService {
    private final PetRepository petRepository;
    private final BreedRepository breedRepository;

    public Pet getPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PET_NOT_FOUND));
    }

    public List<Pet> getPetsByMember(Member member) {
        return petRepository.findByMember(member);
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
