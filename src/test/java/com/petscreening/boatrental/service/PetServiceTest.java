package com.petscreening.boatrental.service;

import com.petscreening.boatrental.entity.Pet;
import com.petscreening.boatrental.entity.PetOwner;
import com.petscreening.boatrental.enumeration.Species;
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
import com.petscreening.boatrental.util.PetOwnerMapper;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.ArgumentValue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetOwnerService petOwnerService;

    @Mock
    private BoatRentalRulesService boatRentalRulesService;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void testStorePetWithOwner() {
        // Given
        OwnerInput ownerInput = new OwnerInput("12345", "Daniel", "Aguirre", "test@email.com", "55587744");
        PetInput petInput = new PetInput("Max", 20F, "Beagle", 3, true, Species.DOG);
        PetWithOwnerInput input = new PetWithOwnerInput(petInput, ownerInput);

        PetOwner petOwner = PetOwnerMapper.mapOwnerInputToEntity(ownerInput);
        Pet pet = PetMapper.mapPetInputToEntity(petInput);
        pet.setOwner(petOwner);

        given(petOwnerService.save(ownerInput)).willReturn(petOwner);
        given(petRepository.save(pet)).willReturn(pet);

        // When
        Pet result = petService.storePetWithOwner(input);

        // Then
        assertEquals(pet, result);
        then(petRepository).should().save(pet);
        then(petOwnerService).should().save(ownerInput);
    }

    @Test
    public void testStorePetWithOwnerThatAlreadyExists() {
        // Given
        OwnerInput ownerInput = new OwnerInput("12345", "Daniel", "Aguirre", "test@email.com", "55587744");
        PetInput petInput = new PetInput("Max", 20F, "Beagle", 3, true, Species.DOG);
        PetWithOwnerInput input = new PetWithOwnerInput(petInput, ownerInput);

        PetOwner petOwner = PetOwnerMapper.mapOwnerInputToEntity(ownerInput);
        Pet pet = PetMapper.mapPetInputToEntity(petInput);
        pet.setOwner(petOwner);

        given(petOwnerService.findByGovernmentId("12345")).willReturn(Optional.of(petOwner));

        // When - Then
        assertThrows(PetOwnerAlreadyRegisteredException.class, ()->petService.storePetWithOwner(input));

    }

    @Test
    public void testStorePetForExistingOwner() {
        // Given
        PetInput petInput = new PetInput("Max", 20F, "Beagle", 3, true, Species.DOG);
        PetWithOwnerIdInput input = new PetWithOwnerIdInput(petInput, "12345");
        PetOwner petOwner = PetOwner.builder().build();
        Pet pet = PetMapper.mapPetInputToEntity(petInput);
        pet.setOwner(petOwner);

        given(petOwnerService.findByGovernmentId(input.getOwnerGovernmentId())).willReturn(Optional.of(petOwner));
        given(petRepository.save(pet)).willReturn(pet);

        // When
        Pet result = petService.storePetForExistingOwner(input);

        // Then
        assertEquals(pet, result);
        then(petRepository).should().save(pet);
    }

    @Test
    public void testStorePetWithUnexistingOwner() {
        // Given
        PetInput petInput = new PetInput("Max", 20F, "Beagle", 3, true, Species.DOG);
        PetWithOwnerIdInput input = new PetWithOwnerIdInput(petInput, "12345");
        PetOwner petOwner = PetOwner.builder().build();
        Pet pet = PetMapper.mapPetInputToEntity(petInput);
        pet.setOwner(petOwner);

        given(petOwnerService.findByGovernmentId(input.getOwnerGovernmentId())).willReturn(Optional.empty());

        // When - Then
        assertThrows(PetOwnerNotFoundException.class, () ->petService.storePetForExistingOwner(input));

    }

    @Test
    public void testUpdatePet() {
        // Given
        PetOwner petOwner = PetOwner.builder().build();
        Long id = 1L;
        PetInputToUpdate petInput = new PetInputToUpdate(id, "Max", 20F, "Beagle", 3, true, Species.DOG);
        Pet pet = new Pet();
        pet.setId(id);
        pet.setOwner(petOwner);

        Pet updatedPet = PetMapper.mapPetInputToEntityUsingOriginalPet(pet, petInput);
        updatedPet.setId(id);
        updatedPet.setOwner(petOwner);

        // When
        given(petRepository.findById(id)).willReturn(Optional.of(pet));
        given(petRepository.save(updatedPet)).willReturn(updatedPet);

        Pet result = petService.updatePet(petInput);

        // Then
        assertEquals(updatedPet, result);
        then(petRepository).should().save(updatedPet);
    }

    @Test
    public void testUpdatePetWithUnexistingPet() {
        // Given
        PetOwner petOwner = PetOwner.builder().build();
        Long id = 1L;
        PetInputToUpdate petInput = new PetInputToUpdate(id, "Max", 20F, "Beagle", 3, true, Species.DOG);
        Pet pet = new Pet();
        pet.setId(id);
        pet.setOwner(petOwner);

        Pet updatedPet = PetMapper.mapPetInputToEntityUsingOriginalPet(pet, petInput);
        updatedPet.setId(id);
        updatedPet.setOwner(petOwner);

        // When - Then
        given(petRepository.findById(id)).willReturn(Optional.empty());


        assertThrows(PetNotFoundException.class, () -> petService.updatePet(petInput));

    }

    @Test
    public void testFindPetsByFilters() {
        // Given
        Float maxWeightInPounds = 20F;
        Boolean vaccinated = true;
        String excludedBreed = "Beagle";
        Integer minTrainingLevel = 3;
        List<Pet> expectedPets = Arrays.asList(new Pet(), new Pet());

        given(petRepository.findAll(any(Specification.class))).willReturn(expectedPets);

        // When
        List<Pet> result = petService.findPetsByFilters(maxWeightInPounds, vaccinated, excludedBreed, minTrainingLevel);

        // Then
        assertEquals(expectedPets, result);
    }

    @Test
    public void testChangeOwnerToPetWithOwnerId() {
        // Given
        Long petId = 1L;
        ArgumentValue<Long> ownerId = ArgumentValue.ofNullable(2L);
        ArgumentValue<String> ownerGovernmentId = ArgumentValue.ofNullable(null);
        Pet expectedPet = new Pet();
        expectedPet.setId(petId);
        PetOwner expectedOwner = new PetOwner();

        given(petRepository.findById(petId)).willReturn(Optional.of(expectedPet));
        given(petOwnerService.findById(ownerId.value())).willReturn(Optional.of(expectedOwner));
        given(petRepository.save(expectedPet)).willReturn(expectedPet);

        // When
        Pet result = petService.changeOwnerToPet(petId, ownerId, ownerGovernmentId);

        // Then
        assertEquals(expectedPet, result);
        assertEquals(expectedOwner, result.getOwner());
    }

    @Test
    public void testChangeOwnerByOwnerIdToPet() {
        // Given
        Long petId = 1L;
        ArgumentValue<Long> ownerId = ArgumentValue.ofNullable(2L);
        ArgumentValue<String> ownerGovernmentId = ArgumentValue.ofNullable(null);
        Pet expectedPet = new Pet();
        expectedPet.setId(petId);
        PetOwner expectedOwner = new PetOwner();

        given(petRepository.findById(petId)).willReturn(Optional.of(expectedPet));
        given(petOwnerService.findById(ownerId.value())).willReturn(Optional.of(expectedOwner));
        given(petRepository.save(expectedPet)).willReturn(expectedPet);

        // When
        Pet result = petService.changeOwnerToPet(petId, ownerId, ownerGovernmentId);

        // Then
        assertEquals(expectedPet, result);
        assertEquals(expectedOwner, result.getOwner());
    }

    @Test
    public void testChangeOwnerToPetWithUnexistingOwnerId() {
        // Given
        Long petId = 1L;
        ArgumentValue<Long> ownerId = ArgumentValue.ofNullable(2L);
        ArgumentValue<String> ownerGovernmentId = ArgumentValue.ofNullable(null);
        Pet expectedPet = new Pet();
        expectedPet.setId(petId);


        given(petRepository.findById(petId)).willReturn(Optional.of(expectedPet));
        given(petOwnerService.findById(ownerId.value())).willReturn(Optional.empty());

        // When - Then
        assertThrows(PetOwnerNotFoundException.class, () -> petService.changeOwnerToPet(petId, ownerId, ownerGovernmentId));

    }

    @Test
    public void testChangeOwnerByGovernmentIdToPet() {
        // Given
        Long petId = 1L;
        ArgumentValue<Long> ownerId = ArgumentValue.ofNullable(null);
        ArgumentValue<String> ownerGovernmentId = ArgumentValue.ofNullable("12345");
        Pet expectedPet = new Pet();
        PetOwner expectedOwner = new PetOwner();
        expectedPet.setId(petId);

        given(petRepository.findById(petId)).willReturn(Optional.of(expectedPet));
        given(petOwnerService.findByGovernmentId(ownerGovernmentId.value())).willReturn(Optional.of(expectedOwner));
        given(petRepository.save(expectedPet)).willReturn(expectedPet);

        // When
        Pet result = petService.changeOwnerToPet(petId, ownerId, ownerGovernmentId);

        // Then
        assertEquals(expectedPet, result);
        assertEquals(expectedOwner, result.getOwner());
    }

    @Test
    public void testChangeOwnerToPetWithBothOwnerIdAndGovernmentIdSent() {
        // Given
        Long petId = 1L;
        ArgumentValue<Long> ownerId = ArgumentValue.ofNullable(2L);
        ArgumentValue<String> ownerGovernmentId = ArgumentValue.ofNullable("12345");
        Pet expectedPet = new Pet();

        given(petRepository.findById(petId)).willReturn(Optional.of(expectedPet));

        // When - Then
        assertThrows(ValidationException.class, () -> petService.changeOwnerToPet(petId, ownerId, ownerGovernmentId));

    }

    @Test
    public void testChangeOwnerToPetWithoutOwnerInfoSent() {
        // Given
        Long petId = 1L;
        ArgumentValue<Long> ownerId = ArgumentValue.ofNullable(null);
        ArgumentValue<String> ownerGovernmentId = ArgumentValue.ofNullable(null);
        Pet expectedPet = new Pet();

        given(petRepository.findById(petId)).willReturn(Optional.of(expectedPet));

        // When - Then
        assertThrows(ValidationException.class, () -> petService.changeOwnerToPet(petId, ownerId, ownerGovernmentId));

    }

    @Test
    public void testIsPetEligibleForRental() {
        // Given
        Long petId = 1L;
        Pet expectedPet = new Pet();
        PetEligibility expectedEligibility = new PetEligibility();

        given(petRepository.findById(petId)).willReturn(Optional.of(expectedPet));
        given(boatRentalRulesService.isPetEligibleForRental(expectedPet)).willReturn(expectedEligibility);

        // When
        PetEligibility result = petService.isPetEligibleForRental(petId);

        // Then
        assertEquals(expectedEligibility, result);
    }

}