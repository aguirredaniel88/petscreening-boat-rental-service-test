package com.petscreening.boatrental.graphql.input;

import com.petscreening.boatrental.enumeration.Species;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetInput {
    @NotEmpty
    private String name;
    private Float weight;
    private String breed;
    @Min(1)
    @Max(10)
    private Integer trainingLevel;
    private Boolean vaccinated;
    private Species species;
}
