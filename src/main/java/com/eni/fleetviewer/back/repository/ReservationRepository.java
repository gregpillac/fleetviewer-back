package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Trouve les réservations pour un véhicule donné qui chevauchent une période donnée,
     */
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.vehicle.id = :vehicleId " +
            "AND r.startDate < :endDate AND r.endDate > :startDate")
    List<Reservation> findByVehicleIdAndDates(@Param("vehicleId") Long vehicleId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Reservation> findByDriverIdOrderByStartDateDesc(Long driverId);
}