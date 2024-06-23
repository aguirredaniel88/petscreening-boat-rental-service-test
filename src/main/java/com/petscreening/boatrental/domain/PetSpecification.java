package com.petscreening.boatrental.domain;

import com.petscreening.boatrental.entity.Pet;
import org.springframework.data.jpa.domain.Specification;

public class PetSpecification {
    public static Specification<Pet> weightLessThanOrEqual(double weight) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("weight"), weight);
    }

    public static Specification<Pet> vaccinated(boolean vaccinated) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isVaccinated"), vaccinated);
    }

    public static Specification<Pet> breedNot(String breed) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("breed"), breed);
    }

    public static Specification<Pet> trainingLevelGreaterThanOrEqual(int level) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("trainingLevel"), level);
    }
}
