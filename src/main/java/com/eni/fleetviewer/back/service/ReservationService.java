package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.ReservationDTO;
import com.eni.fleetviewer.back.enums.Status;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.ReservationMapper;
import com.eni.fleetviewer.back.model.Reservation;
import com.eni.fleetviewer.back.model.ReservationStatus;
import com.eni.fleetviewer.back.repository.ReservationRepository;
import com.eni.fleetviewer.back.repository.ReservationStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationStatusRepository reservationStatusRepository;


    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper, ReservationStatusRepository reservationStatusRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.reservationStatusRepository = reservationStatusRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Réservation non trouvée pour l'id " + id));
        return reservationMapper.toDto(reservation);
    }


    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {

        /// 1. Validation des dates ///////////////////////////////////////////////////////////////////
        if (reservationDTO.getStartDate().isAfter(reservationDTO.getEndDate())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
        }

        /// 2. Vérification des conflits avec les réservations déjà confirmées ////////////////////////
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(
                reservationDTO.getVehicleId(),
                reservationDTO.getStartDate(),
                reservationDTO.getEndDate()
        );

        if (!conflictingReservations.isEmpty()) {
            throw new IllegalStateException("Le véhicule est déjà réservé sur cette période.");
        }

        /// 3. Conversion du DTO en entité et définition du statut 'PENDING' par défaut ///////////////
        Reservation reservation = reservationMapper.toEntity(reservationDTO);

        ReservationStatus pendingStatus = reservationStatusRepository.findByName(Status.PENDING.getStatus())
                .orElseThrow(() -> new IllegalStateException("Le statut par défaut 'En attente' n'a pas été trouvé en base de données."));
        reservation.setReservationStatus(pendingStatus);

        /// 4. Sauvegarde de la nouvelle réservation /////////////////////////////////////////////////
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(savedReservation);
    }


    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Réservation non trouvée pour l'id " + id));

        /// On n'autorise la mise à jour du Status et du Conducteur uniquement
        reservation.setReservationStatus(reservationMapper.longToReservationStatus(reservationDTO.getReservationStatusId()));
        reservation.setDriver(reservationMapper.longToDriver(reservationDTO.getDriverId()));

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(savedReservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RessourceNotFoundException("Réservation non trouvée pour l'id " + id);
        }
        reservationRepository.deleteById(id);
    }

}
