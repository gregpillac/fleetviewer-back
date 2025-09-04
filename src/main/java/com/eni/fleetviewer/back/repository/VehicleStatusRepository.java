package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleStatusRepository extends JpaRepository<VehicleStatus, Long> {
}