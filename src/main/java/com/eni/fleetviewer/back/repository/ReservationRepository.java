package com.eni.fleetviewer.back.repository;

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