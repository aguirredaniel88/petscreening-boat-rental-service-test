package com.petscreening.boatrental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PET_OWNER")
@SequenceGenerator(name="owner_seq", sequenceName="OWNER_SEQUENCE", allocationSize=1)
public class PetOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="owner_seq")
    private Long id;

    @Column(name = "government_id")
    private String governmentId;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

}
