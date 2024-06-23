package com.petscreening.boatrental.graphql.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetWithOwnerInput {
    @NotNull
    @Valid
    private PetInput pet;
    @NotNull
    @Valid
    private OwnerInput owner;
}
