package com.petscreening.boatrental.util;

import com.petscreening.boatrental.entity.Pet;
import com.petscreening.boatrental.graphql.input.PetInput;
import com.petscreening.boatrental.graphql.input.PetInputToUpdate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetMapper {

    public static Pet mapPetInputToEntity(PetInput pet) {
        return Pet.builder()
                .name(pet.getName())
                .weight(pet.getWeight())
                .isVaccinated(pet.getVaccinated())
                .trainingLevel(pet.getTrainingLevel())
                .breed(pet.getBreed())
                .species(pet.getSpecies())
                .build();
    }

    public static Pet mapPetInputToEntityUsingOriginalPet(Pet petToUpdate, PetInputToUpdate petInput) {
        Pet.PetBuilder petBuilder = petToUpdate.toBuilder();

        if (petInput.getName() != null) {
            petBuilder.name(petInput.getName());
        }
        if (petInput.getWeight() != null) {
            petBuilder.weight(petInput.getWeight());
        }
        if (petInput.getVaccinated() != null) {
            petBuilder.isVaccinated(petInput.getVaccinated());
        }
        if (petInput.getTrainingLevel() != null) {
            petBuilder.trainingLevel(petInput.getTrainingLevel());
        }
        if (petInput.getBreed() != null) {
            petBuilder.breed(petInput.getBreed());
        }
        if (petInput.getSpecies() != null) {
            petBuilder.species(petInput.getSpecies());
        }

        return petBuilder.build();
    }
}
