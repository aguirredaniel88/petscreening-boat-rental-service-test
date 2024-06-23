package com.petscreening.boatrental.service;

import com.petscreening.boatrental.entity.Pet;
import com.petscreening.boatrental.entity.PetOwner;
import com.petscreening.boatrental.exception.PetNotFoundException;
import com.petscreening.boatrental.exception.PetOwnerAlreadyRegisteredException;
import com.petscreening.boatrental.exception.PetOwnerNotFoundException;
import com.petscreening.boatrental.graphql.input.OwnerInput;
import com.petscreening.boatrental.graphql.input.PetInput;
import com.petscreening.boatrental.graphql.input.PetInputToUpdate;
import com.petscreening.boatrental.graphql.input.PetWithOwnerIdInput;
import com.petscreening.boatrental.graphql.input.PetWithOwnerInput;
import com.petscreening.boatrental.graphql.output.PetEligibility;
import com.petscreening.boatrental.repository.PetRepository;
import com.petscreening.boatrental.util.PetEligibilitySpecBuilder;
import com.petscreening.boatrental.util.PetMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.ArgumentValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private static final String PET_OWNER_ID_NOT_FOUND = "Pet owner with id %s not found in the system";
    private static final String PET_OWNER_GOVERNMENT_ID_NOT_FOUND = "Pet owner with governmentId %s not found in the system";
    private static final String PET_OWNER_ALREADY_REGISTERED = "Pet owner with governmentId %s is already registered in the system";
    private static final String PET_NOT_FOUND = "Pet with id %d not found in the system";
    private static final String OWNER_ID_OR_GOVERNMENT_ID_NEEDED = "You must provide exactly one of ownerId or ownerGovernmentId, but not both.";

    private final PetRepository petRepository;
    private final PetOwnerService petOwnerService;
    private final BoatRentalRulesService boatRentalRulesService;


    public List<Pet> findPetsByFilters(Float maxWeightInPounds, Boolean vaccinated, String excludedBreed, Integer minTrainingLevel) {
        Specification<Pet> filtersSpec = PetEligibilitySpecBuilder.buildFiltersSpec(maxWeightInPounds, vaccinated, excludedBreed, minTrainingLevel);
        return petRepository.findAll(filtersSpec);
    }


    @Transactional
    public Pet storePetWithOwner(PetWithOwnerInput petWithOwnerInput) {
        OwnerInput ownerInput = petWithOwnerInput.getOwner();
        validateOwnerDoesNotExistWithSameGovernmentId(ownerInput);
        PetOwner petOwner = petOwnerService.save(ownerInput);
        Pet pet = createPetToSave(petWithOwnerInput.getPet(), petOwner);
        return petRepository.save(pet);
    }

    public Pet storePetForExistingOwner(PetWithOwnerIdInput petWithOwnerIdInput) {
        PetOwner petOwner = findPetOwnerByGovernmentIdOrThrowException(petWithOwnerIdInput.getOwnerGovernmentId());
        Pet pet = createPetToSave(petWithOwnerIdInput.getPet(), petOwner);
        return petRepository.save(pet);
    }

    public Pet updatePet(PetInputToUpdate petInput) {
        Pet petFromDb = getPetFromDbOrThrowException(petInput.getId());
        Pet petToSave = PetMapper.mapPetInputToEntityUsingOriginalPet(petFromDb, petInput);
        return petRepository.save(petToSave);
    }

    private static Pet createPetToSave(PetInput petInput, PetOwner petOwner) {
        Pet petToSave = PetMapper.mapPetInputToEntity(petInput);
        petToSave.setOwner(petOwner);
        return petToSave;
    }


    public Pet changeOwnerToPet(Long petId, ArgumentValue<Long> ownerId,  ArgumentValue<String> ownerGovernmentId) {
        Pet pet = getPetFromDbOrThrowException(petId);
        pet.setOwner(getPetOwner(ownerId, ownerGovernmentId));
        return petRepository.save(pet);
    }

    public PetEligibility isPetEligibleForRental(Long petId) {
        Pet pet = getPetFromDbOrThrowException(petId);
        return boatRentalRulesService.isPetEligibleForRental(pet);
    }

    private Pet getPetFromDbOrThrowException(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(String.format(PET_NOT_FOUND, id)));
    }

    private void validateOwnerDoesNotExistWithSameGovernmentId(OwnerInput ownerInput) {
        Optional<PetOwner> firstByGovernmentId = findPetOwnerByGovernmentId(ownerInput.getGovernmentId());
        if(firstByGovernmentId.isPresent()) {
            throw new PetOwnerAlreadyRegisteredException(String.format(PET_OWNER_ALREADY_REGISTERED, ownerInput.getGovernmentId()));
        }
    }

    private Optional<PetOwner> findPetOwnerByGovernmentId(String governmentId) {
        return petOwnerService.findByGovernmentId(governmentId);
    }

    private PetOwner findPetOwnerByGovernmentIdOrThrowException(String governmentId) {
        return findPetOwnerByGovernmentId(governmentId).orElseThrow(() ->
                new PetOwnerNotFoundException(String.format(PET_OWNER_GOVERNMENT_ID_NOT_FOUND, governmentId)));

    }

    private PetOwner findPetOwnerByIdOrThrowException(Long ownerId) {
        return petOwnerService.findById(ownerId).orElseThrow(() ->
                new PetOwnerNotFoundException(String.format(PET_OWNER_ID_NOT_FOUND, ownerId)));

    }

    private PetOwner getPetOwner(ArgumentValue<Long> ownerId, ArgumentValue<String> ownerGovernmentId) {
        validateOwnerIdAndGovernmentId(ownerId, ownerGovernmentId);
        return ownerId.isPresent() ? findPetOwnerByIdOrThrowException(ownerId.value())
            : findPetOwnerByGovernmentIdOrThrowException(ownerGovernmentId.value());

    }


    private static void validateOwnerIdAndGovernmentId(ArgumentValue<Long> ownerId, ArgumentValue<String> ownerGovernmentId) {
        if ((ownerId.isOmitted() && ownerGovernmentId.isOmitted())
            || ownerId.isPresent() && ownerGovernmentId.isPresent()) {
            throw new ValidationException(OWNER_ID_OR_GOVERNMENT_ID_NEEDED);
        }
    }
}
