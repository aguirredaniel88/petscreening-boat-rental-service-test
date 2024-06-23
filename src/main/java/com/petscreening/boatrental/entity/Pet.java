package com.petscreening.boatrental.entity;

import com.petscreening.boatrental.enumeration.Species;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PET")
@SequenceGenerator(name="pet_seq", sequenceName="PET_SEQUENCE", allocationSize=1)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="pet_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PetOwner owner;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "breed")
    private String breed;

    @Column(name = "training_level")
    private Integer trainingLevel;

    @Column(name = "is_vaccinated")
    private Boolean isVaccinated;

    @Column(name = "species")
    @Enumerated(EnumType.STRING)
    private Species species;

}
