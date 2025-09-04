package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceTypeRepository extends JpaRepository<PlaceType, Long> {
    // JpaRepository fournit déjà : save(), findById(), findAll(), deleteById(), etc.
}