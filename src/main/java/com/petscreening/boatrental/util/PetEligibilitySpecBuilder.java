package com.petscreening.boatrental.util;

import com.petscreening.boatrental.domain.PetSpecification;
import com.petscreening.boatrental.entity.Pet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetEligibilitySpecBuilder {

    public static Specification<Pet> buildFiltersSpec(Float maxWeightInPounds, Boolean vaccinated, String excludedBreed, Integer minTrainingLevel) {
        Specification<Pet> spec = Specification.where(null);

        if (maxWeightInPounds != null) {
            spec = spec.and(PetSpecification.weightLessThanOrEqual(maxWeightInPounds));
        }
        if (vaccinated != null) {
            spec = spec.and(PetSpecification.vaccinated(vaccinated));
        }
        if (excludedBreed != null) {
            spec = spec.and(PetSpecification.breedNot(excludedBreed));
        }
        if (minTrainingLevel != null) {
            spec = spec.and(PetSpecification.trainingLevelGreaterThanOrEqual(minTrainingLevel));
        }
        return spec;
    }
}
