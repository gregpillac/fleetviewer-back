package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.dto.ReservationDTO;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.enums.Status;
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
              ((r.departure.id = :requestedDeparturePlaceId
                OR r.departure.id = :requestedArrivalPlaceId)
               AND r.startDate >= :departureStartOfDay
               AND r.startDate <  :departureEndOfDay)
           OR (r.arrival.id = :requestedDeparturePlaceId
               OR r.arrival.id = :requestedArrivalPlaceId)
               AND r.endDate >= :departureStartOfDay
               AND r.endDate <  :departureEndOfDay)
                      AND r.reservationStatus = 'CONFIRMED'
       """)
    List<Reservation> findCompatibleReservationsOnStartDateAndPlace(
            @Param("requestedDeparturePlaceId") Long departurePlaceId,
            @Param("requestedArrivalPlaceId") Long arrivalPlaceId,
            @Param("departureStartOfDay") LocalDateTime departureStartOfDay,
            @Param("departureEndOfDay")   LocalDateTime departureEndOfDay);

    //TOD0: à implémenter Apres ajout du nb de places restantes dans la Réservation
    //List<ReservationDTO> findCompatibleReservationsOnStartDateAndPlaceAndSeatsLeft();


    /**
     * Recherche les réservations par statut, avec un filtre optionnel sur le lieu.
     * La requête sélectionne les réservations correspondant au statut fourni.
     * Si un `placeId` est spécifié :
     *   - on vérifie que le véhicule associé (s'il existe) appartient à ce lieu
     *   - sinon, si le véhicule est absent mais qu'un conducteur est lié, on vérifie que ce conducteur appartient à ce lieu
     * Si `placeId` est null, aucune restriction sur le lieu n'est appliquée.
     * Le résultat est trié par date de début (startDate) croissante.
     *
     * @param status  le statut de la réservation (obligatoire)
     * @param placeId l'identifiant du lieu (optionnel, peut être null)
     * @return la liste des réservations correspondant aux critères
     */
    @Query("""
      select r from Reservation r
      left join r.vehicle v
      left join r.driver d
      where r.reservationStatus = :status
        and (
          :placeId is null
          or (v is not null and v.place.id = :placeId)
          or (v is null and d is not null and d.place.id = :placeId)
        )
      order by r.startDate asc
    """)
    List<Reservation> findByStatusAndOptionalPlace(@Param("status") Status status,
                                                   @Param("placeId") Long placeId);

    @Query("""
      select count(r) = 0 from Reservation r
      where r.vehicle.id = :vehicleId
        and r.reservationStatus in (:confirmed, :pending)
        and (r.startDate < :end and r.endDate > :start)
    """)
    boolean isVehicleFree(@Param("vehicleId") Long vehicleId,
                          @Param("start") LocalDateTime start,
                          @Param("end") LocalDateTime end,
                          @Param("confirmed") Status confirmed,
                          @Param("pending") Status pending);

}