package com.petscreening.boatrental.graphql.output;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
public class PetEligibility {
    private Boolean isEligibleForRental = true;
    private Set<String> reasons;

    public void addReason(String reason) {
        if (reasons == null) {
            reasons = new HashSet<>();
        }
        reasons.add(reason);
        isEligibleForRental = false;
    }
}
