package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleStatusRepository extends JpaRepository<VehicleStatus, Long> {
}