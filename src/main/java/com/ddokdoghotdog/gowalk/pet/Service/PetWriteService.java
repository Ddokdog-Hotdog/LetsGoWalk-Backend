package com.ddokdoghotdog.gowalk.pet.Service;

import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.entity.Breed;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PetWriteService {
    private final PetRepository petRepository;
    private final PetReadService petReadService;

    @Transactional
    public PetDTO.Response createPet(PetDTO.CreateRequest petCreateRequestDTO) {
        Member member = Member.builder().id(petCreateRequestDTO.getMemberId()).build();
        Breed breed = petReadService.getBreedById(petCreateRequestDTO.getBreedId());
        Pet pet = petCreateRequestDTO.toEntity(member, breed);
        petRepository.save(pet);

        return PetDTO.Response.of(pet);
    };

    @Transactional
    public PetDTO.Response updatePet(PetDTO.Update petUpdateRequsetDTO) {
        Pet pet = petReadService.getPetByIdAndMemberId(petUpdateRequsetDTO.getPetId(),
                petUpdateRequsetDTO.getMemberId());
        Breed breed = Breed.builder().id(petUpdateRequsetDTO.getBreedId()).build();
        pet.updatePet(petUpdateRequsetDTO, breed);
        petRepository.save(pet);

        return PetDTO.Response.of(pet);
    }

    @Transactional
    public void deletePet(Long petId, Long memberId) {
        petRepository.deleteByPetIdAndMemberId(petId, memberId);
    }
}
