package com.petscreening.boatrental.service;

import com.petscreening.boatrental.entity.Pet;
import com.petscreening.boatrental.graphql.output.PetEligibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoatRentalRulesServiceTest {

    private BoatRentalRulesService boatRentalRulesService;

    @Mock
    private Pet pet;

    @BeforeEach
    public void setup() {
        boatRentalRulesService = new BoatRentalRulesService(true, 50.0F, 3, Arrays.asList("Breed1", "Breed2"));
    }

    @Test
    public void testIsPetEligibleForRental() {
        given(pet.getIsVaccinated()).willReturn(true);
        given(pet.getWeight()).willReturn(45.0f);
        given(pet.getTrainingLevel()).willReturn(4);
        given(pet.getBreed()).willReturn("Breed3");

        PetEligibility petEligibility = boatRentalRulesService.isPetEligibleForRental(pet);

        assertNull(petEligibility.getReasons());
        assertTrue(petEligibility.getIsEligibleForRental());
    }

    @Test
    public void testIsPetNotEligibleForRental() {
        given(pet.getIsVaccinated()).willReturn(false);
        given(pet.getWeight()).willReturn(55.0f);
        given(pet.getTrainingLevel()).willReturn(2);
        given(pet.getBreed()).willReturn("Breed1");

        PetEligibility petEligibility = boatRentalRulesService.isPetEligibleForRental(pet);

        assertEquals(4, petEligibility.getReasons().size());
        assertFalse(petEligibility.getIsEligibleForRental());
    }

    @Test
    public void testIsPetNotEligibleForRentalWhenNoDataIsFound() {
        given(pet.getIsVaccinated()).willReturn(false);
        given(pet.getWeight()).willReturn(null);
        given(pet.getTrainingLevel()).willReturn(null);
        given(pet.getBreed()).willReturn(null);
        PetEligibility petEligibility = boatRentalRulesService.isPetEligibleForRental(pet);
        assertEquals(4, petEligibility.getReasons().size());
        assertFalse(petEligibility.getIsEligibleForRental());
    }

    @Test
    public void testIsPetNotEligibleForRentalWithOnlyVaccinatedFalse() {
        boatRentalRulesService = new BoatRentalRulesService(false, 50.0F, 3, Arrays.asList("Breed1", "Breed2"));

        given(pet.getWeight()).willReturn(55.0f);
        given(pet.getTrainingLevel()).willReturn(2);
        given(pet.getBreed()).willReturn("Breed1");

        PetEligibility petEligibility = boatRentalRulesService.isPetEligibleForRental(pet);

        assertEquals(3, petEligibility.getReasons().size());
        assertFalse(petEligibility.getIsEligibleForRental());
    }
}
