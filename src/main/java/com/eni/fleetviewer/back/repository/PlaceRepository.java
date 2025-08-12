package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PlaceRepository extends JpaRepository<Place, Long> {

}
