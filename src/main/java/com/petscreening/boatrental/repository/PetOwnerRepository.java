package com.petscreening.boatrental.repository;

import com.petscreening.boatrental.entity.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {

    Optional<PetOwner> findFirstByGovernmentId(String governmentId);
}
