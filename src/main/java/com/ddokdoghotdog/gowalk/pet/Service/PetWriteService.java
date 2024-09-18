package com.ddokdoghotdog.gowalk.pet.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ddokdoghotdog.gowalk.entity.Breed;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Pet;
import com.ddokdoghotdog.gowalk.global.config.s3.S3Service;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PetWriteService {
    private final PetRepository petRepository;
    private final PetReadService petReadService;
    private final S3Service s3Service;

    @Transactional
    public PetDTO.Response createPet(PetDTO.CreateRequest petCreateRequestDTO, MultipartFile profileImage) {
        Member member = Member.builder().id(petCreateRequestDTO.getMemberId()).build();
        Breed breed = petReadService.getBreedById(petCreateRequestDTO.getBreedId());
        String profileImageUrl ="";
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageUrl = s3Service.uploadFile(profileImage, "pets");
        }
        Pet pet = petCreateRequestDTO.toEntity(member, breed, profileImageUrl);
        
        petRepository.save(pet);

        return PetDTO.Response.of(pet);
    };

    @Transactional
    public PetDTO.Response updatePet(PetDTO.Update petUpdateRequestDTO, MultipartFile profileImage) {
        Pet pet = petReadService.getPetByIdAndMemberId(petUpdateRequestDTO.getPetId(),
        		petUpdateRequestDTO.getMemberId());
        Breed breed = petReadService.getBreedById(petUpdateRequestDTO.getBreedId());
        
        String profileImageUrl = pet.getProfileImageUrl(); // Keep the current URL if no new image is provided
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageUrl = s3Service.uploadFile(profileImage, "pets");
        }
        pet.updatePet(petUpdateRequestDTO, breed, profileImageUrl);
        petRepository.save(pet);

        return PetDTO.Response.of(pet);
    }

    @Transactional
    public void deletePet(Long petId, Long memberId) {
        petRepository.deleteByPetIdAndMemberId(petId, memberId);
    }

}
