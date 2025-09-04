package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
    Optional<ReservationStatus> findByName(String name);
}