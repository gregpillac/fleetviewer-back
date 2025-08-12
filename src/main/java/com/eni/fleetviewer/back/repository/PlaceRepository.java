package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
