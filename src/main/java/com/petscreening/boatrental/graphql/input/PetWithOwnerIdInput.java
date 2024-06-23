package com.petscreening.boatrental.graphql.input;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetWithOwnerIdInput {
    @NotNull
    private PetInput pet;
    @NotNull
    private String ownerGovernmentId;
}
