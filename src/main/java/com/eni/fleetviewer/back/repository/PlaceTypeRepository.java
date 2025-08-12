package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTypeRepository extends JpaRepository<PlaceType, Long> {
    // JpaRepository fournit déjà : save(), findById(), findAll(), deleteById(), etc.
}