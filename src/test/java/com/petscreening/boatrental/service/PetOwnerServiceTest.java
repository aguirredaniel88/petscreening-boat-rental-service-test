package com.petscreening.boatrental.service;

import com.petscreening.boatrental.entity.PetOwner;
import com.petscreening.boatrental.exception.PetOwnerAlreadyRegisteredException;
import com.petscreening.boatrental.graphql.input.OwnerInput;
import com.petscreening.boatrental.repository.PetOwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PetOwnerServiceTest {

    @Mock
    private PetOwnerRepository petOwnerRepository;

    @InjectMocks
    private PetOwnerService petOwnerService;

    private PetOwner petOwner;
    private OwnerInput ownerInput;

    @BeforeEach
    public void setUp() {
        petOwner = new PetOwner();
        petOwner.setGovernmentId("12345");

        ownerInput = new OwnerInput();
        ownerInput.setGovernmentId("12345");
    }

    @Test
    public void shouldFindPetOwnerByGovernmentId() {
        given(petOwnerRepository.findFirstByGovernmentId(anyString())).willReturn(Optional.of(petOwner));

        Optional<PetOwner> result = petOwnerService.findByGovernmentId("12345");

        assertTrue(result.isPresent());
        assertEquals(petOwner, result.get());
    }

    @Test
    public void shouldNotFindPetOwnerByGovernmentId() {
        given(petOwnerRepository.findFirstByGovernmentId(anyString())).willReturn(Optional.empty());

        Optional<PetOwner> result = petOwnerService.findByGovernmentId("12345");

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldSavePetOwner() {
        given(petOwnerRepository.findFirstByGovernmentId(anyString())).willReturn(Optional.empty());
        given(petOwnerRepository.save(any(PetOwner.class))).willReturn(petOwner);

        PetOwner result = petOwnerService.save(ownerInput);

        assertEquals(petOwner, result);
    }

    @Test
    public void shouldThrowExceptionWhenSaveExistingPetOwner() {
        given(petOwnerRepository.findFirstByGovernmentId(anyString())).willReturn(Optional.of(petOwner));

        assertThrows(PetOwnerAlreadyRegisteredException.class, () -> petOwnerService.save(ownerInput));
    }

    @Test
    public void findByIdShouldCallRepository() {
        long id = 1L;
        PetOwner potOwner = PetOwner.builder().id(id).build();
        given(petOwnerRepository.findById(id)).willReturn(Optional.of(potOwner));

        Optional<PetOwner> result = petOwnerService.findById(id);

        assertEquals(potOwner, result.get());
    }
}
