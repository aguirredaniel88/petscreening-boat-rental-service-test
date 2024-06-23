package com.petscreening.boatrental.service;

import com.petscreening.boatrental.entity.Pet;
import com.petscreening.boatrental.graphql.output.PetEligibility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoatRentalRulesService {

    private static final String PET_DOES_NOT_HAVE_MINIMUM_TRAINING_LEVEL = "Pet does not have minimum training level (%d)";
    private static final String PET_IS_NOT_WITHIN_WEIGHT_LIMIT = "Pet is not within weight limit (%f)";
    private static final String PET_BREED_IS_RESTRICTED = "Pet breed is restricted";
    private static final String PET_IS_NOT_VACCINATED = "Pet is not vaccinated";

    private final boolean onlyVaccinated;
    private final float maxWeightInPounds;
    private final int minTrainingLevel;
    private final List<String> restrictedBreeds;

    public BoatRentalRulesService(@Value("${rules.only-vaccinated-pets}") boolean onlyVaccinated,
                                  @Value("${rules.max-weight-in-pounds}") float maxWeightInPounds,
                                  @Value("${rules.min-training-level}")int minTrainingLevel,
                                  @Value("${rules.restricted-breeds}")List<String> restrictedBreeds) {
        this.onlyVaccinated = onlyVaccinated;
        this.maxWeightInPounds = maxWeightInPounds;
        this.minTrainingLevel = minTrainingLevel;
        this.restrictedBreeds = restrictedBreeds;
    }

    public PetEligibility isPetEligibleForRental(Pet pet) {
        PetEligibility petEligibility = new PetEligibility();
        if (isVaccinatedStatusInvalid(pet)) {
            petEligibility.addReason(PET_IS_NOT_VACCINATED);
        }
        if (doesNotHaveMinimumTrainingLevel(pet)) {
            petEligibility.addReason(String.format(PET_DOES_NOT_HAVE_MINIMUM_TRAINING_LEVEL, minTrainingLevel));
        }
        if (isNotWithinWeightLimit(pet)){
            petEligibility.addReason(String.format(PET_IS_NOT_WITHIN_WEIGHT_LIMIT, maxWeightInPounds));
        }
        if (isRestrictedBreed(pet)) {
            petEligibility.addReason(PET_BREED_IS_RESTRICTED);
        }
        return petEligibility;
    }

    private boolean doesNotHaveMinimumTrainingLevel(Pet pet) {
        return pet.getTrainingLevel() == null || pet.getTrainingLevel() < minTrainingLevel;
    }

    private boolean isNotWithinWeightLimit(Pet pet) {
        return pet.getWeight() == null || pet.getWeight() > maxWeightInPounds;
    }

    private boolean isRestrictedBreed(Pet pet) {
        return restrictedBreeds.stream().map(String::toUpperCase)
                .anyMatch(breed -> pet.getBreed() == null || pet.getBreed().equalsIgnoreCase(breed));
    }

    private boolean isVaccinatedStatusInvalid(Pet pet) {
        if (onlyVaccinated) {
            return !pet.getIsVaccinated();
        }
        return false;
    }
}
