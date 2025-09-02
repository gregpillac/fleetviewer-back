package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.ReservationDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.ReservationMapper;
import com.eni.fleetviewer.back.model.Reservation;
import com.eni.fleetviewer.back.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
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
        // TODO: Ajouter la logique de validation métier.
        // Vérifier que le véhicule n'est pas déjà réservé sur ces dates.
        // Vérifier que la date de fin est après la date de début.
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(savedReservation);
    }

    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Réservation non trouvée pour l'id " + id));

        existingReservation.setStartDate(reservationDTO.getStartDate());
        existingReservation.setEndDate(reservationDTO.getEndDate());
        existingReservation.setReservationStatus(reservationMapper.longToReservationStatus(reservationDTO.getReservationStatusId()));
        existingReservation.setVehicle(reservationMapper.longToVehicle(reservationDTO.getVehicleId()));
        existingReservation.setDriver(reservationMapper.longToDriver(reservationDTO.getDriverId()));

        Reservation savedReservation = reservationRepository.save(existingReservation);
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
