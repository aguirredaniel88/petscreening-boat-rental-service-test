package com.petscreening.boatrental.service;

import com.petscreening.boatrental.entity.PetOwner;
import com.petscreening.boatrental.exception.PetOwnerAlreadyRegisteredException;
import com.petscreening.boatrental.graphql.input.OwnerInput;
import com.petscreening.boatrental.repository.PetOwnerRepository;
import com.petscreening.boatrental.util.PetOwnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetOwnerService {

    private static final String PET_OWNER_GOVERNMENT_ID_ALREADY_EXISTS = "Pet owner with governmentId %s already exists in the system";
    private final PetOwnerRepository petOwnerRepository;

    public Optional<PetOwner> findByGovernmentId(String governmentId) {
        return petOwnerRepository.findFirstByGovernmentId(governmentId);
    }

    public Optional<PetOwner> findById(Long id) {
        return petOwnerRepository.findById(id);
    }

    public PetOwner save(OwnerInput ownerInput) {
        validatePetOwnerDoesNotExist(ownerInput);
        PetOwner petOwner = PetOwnerMapper.mapOwnerInputToEntity(ownerInput);
        return petOwnerRepository.save(petOwner);
    }

    private void validatePetOwnerDoesNotExist(OwnerInput ownerInput) {
        Optional<PetOwner> owner = findByGovernmentId(ownerInput.getGovernmentId());
        if (owner.isPresent()) {
            throw new PetOwnerAlreadyRegisteredException(String.format(PET_OWNER_GOVERNMENT_ID_ALREADY_EXISTS, ownerInput.getGovernmentId()));
        }
    }


}
