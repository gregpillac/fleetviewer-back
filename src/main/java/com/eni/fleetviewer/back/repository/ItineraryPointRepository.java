package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.ItineraryPoint;
import com.eni.fleetviewer.back.model.id.ItineraryPointId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ItineraryPointRepository extends JpaRepository<ItineraryPoint, ItineraryPointId> {

    List<ItineraryPoint> findByReservationId(Long reservationId);

}