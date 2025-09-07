package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.VehicleKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleKeyRepository extends JpaRepository<VehicleKey, Long> {
    List<VehicleKey> findByVehicle_Id(Long vehicleId);
    List<VehicleKey> findByPlace_Id(Long placeId);
}
