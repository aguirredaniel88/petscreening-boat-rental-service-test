package com.petscreening.boatrental.graphql.controller;

import com.petscreening.boatrental.entity.Pet;
import com.petscreening.boatrental.entity.PetOwner;
import com.petscreening.boatrental.graphql.input.OwnerInput;
import com.petscreening.boatrental.graphql.input.PetInput;
import com.petscreening.boatrental.graphql.input.PetInputToUpdate;
import com.petscreening.boatrental.graphql.input.PetWithOwnerIdInput;
import com.petscreening.boatrental.graphql.input.PetWithOwnerInput;
import com.petscreening.boatrental.graphql.output.PetEligibility;
import com.petscreening.boatrental.service.PetOwnerService;
import com.petscreening.boatrental.service.PetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.ArgumentValue;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PetFriendlyBoatRentalController {

    private final PetService petService;
    private final PetOwnerService petOwnerService;

    @QueryMapping
    public List<Pet> getEligiblePets(@Argument Boolean isVaccinated, @Argument Float maxWeightInPounds,
                                     @Argument Integer minimumTrainingLevel, @Argument String excludedBreed) {
        return petService.findPetsByFilters(maxWeightInPounds, isVaccinated, excludedBreed, minimumTrainingLevel);
    }

    @QueryMapping
    public PetEligibility isPetEligibleForRental(@Argument Long petId) {
        return petService.isPetEligibleForRental(petId);
    }

    @MutationMapping
    public Pet createPetAndOwner(@Argument @Valid PetWithOwnerInput petWithOwner) {
        return petService.storePetWithOwner(petWithOwner);
    }

    @MutationMapping
    public Pet createPetAndLinkOwner(@Argument @Valid PetWithOwnerIdInput petWithOwnerId) {
        return petService.storePetForExistingOwner(petWithOwnerId);
    }

    @MutationMapping
    public Pet updatePet(@Argument @Valid PetInputToUpdate petInput) {
        return petService.updatePet(petInput);

    }

    @MutationMapping
    public PetOwner createPetOwner(@Argument @Valid OwnerInput ownerInput) {
        return petOwnerService.save(ownerInput);
    }

    @MutationMapping
    public Pet changeOwnerToPet(@Argument @NotNull Long petId, @Argument ArgumentValue<Long> ownerId,
                                @Argument ArgumentValue<String> ownerGovernmentId) {

        return petService.changeOwnerToPet(petId, ownerId, ownerGovernmentId);
    }
}
