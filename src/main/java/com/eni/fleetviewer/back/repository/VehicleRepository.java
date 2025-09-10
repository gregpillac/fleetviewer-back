package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByPlaceName(String placeName);
    List<Vehicle> findByPlaceId(Long id);
}