package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.dto.ReservationDTO;
import com.eni.fleetviewer.back.model.Place;
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

    /**
     * Réservations compatibles en considérant :
     *  - soit (depart cherché = depart existant ET date de départ le jour de depart existant),
     *  - soit (depart cherché = destination existante ET date de départ le jour de retour existant).
     *
     */
    @Query("""
       SELECT r FROM Reservation r
       WHERE (
              (r.departure.id = :requestedDeparturePlaceId
               AND r.startDate >= :departureStartOfDay
               AND r.startDate <  :departureEndOfDay)
           OR (r.arrival.id = :requestedDeparturePlaceId
               AND r.endDate >= :departureStartOfDay
               AND r.endDate <  :departureEndOfDay)
         )
       """)
    List<Reservation> findCompatibleReservationsOnStartDateAndPlace(
            @Param("requestedDeparturePlaceId") Long departurePlaceId,
            @Param("departureStartOfDay") LocalDateTime departureStartOfDay,
            @Param("departureEndOfDay")   LocalDateTime departureEndOfDay);

    //TOD0: à implémenter Apres ajout du nb de places restantes dans la Réservation
    //List<ReservationDTO> findCompatibleReservationsOnStartDateAndPlaceAndSeatsLeft();
}