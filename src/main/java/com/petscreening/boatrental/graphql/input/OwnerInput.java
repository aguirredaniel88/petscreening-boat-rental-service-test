package com.petscreening.boatrental.graphql.input;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerInput {
    @NotNull
    private String governmentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
