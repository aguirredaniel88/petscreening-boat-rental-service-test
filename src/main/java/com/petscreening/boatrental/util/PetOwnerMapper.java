package com.petscreening.boatrental.util;

import com.petscreening.boatrental.entity.PetOwner;
import com.petscreening.boatrental.graphql.input.OwnerInput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetOwnerMapper {

    public static PetOwner mapOwnerInputToEntity(OwnerInput ownerInput) {
        return PetOwner.builder()
                .governmentId(ownerInput.getGovernmentId())
                .firstName(ownerInput.getFirstName())
                .lastName(ownerInput.getLastName())
                .phoneNumber(ownerInput.getPhoneNumber())
                .email(ownerInput.getEmail())
                .build();
    }
}
