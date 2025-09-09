package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.ItineraryPointDTO;
import com.eni.fleetviewer.back.dto.ReservationDTO;
import com.eni.fleetviewer.back.enums.Status;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.IdToEntityMapper;
import com.eni.fleetviewer.back.mapper.ItineraryPointMapper;
import com.eni.fleetviewer.back.mapper.ReservationMapper;
import com.eni.fleetviewer.back.model.ItineraryPoint;
import com.eni.fleetviewer.back.model.Reservation;
import com.eni.fleetviewer.back.repository.ItineraryPointRepository;
import com.eni.fleetviewer.back.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final IdToEntityMapper idToEntityMapper;
    private final ItineraryPointMapper itineraryPointMapper;
    private final ItineraryPointRepository itineraryPointRepository;


    /**
     * Récupération de toutes les réservations.
     * @return la liste des DTO de réservations
     */
    @Transactional(readOnly = true)
    public List<ReservationDTO> getAllReservations() {
        // Cette méthode ne renvoie pas les points d'itinéraire pour des raisons de performance (éviter N+1 queries).
        // TODO: Faire une méthode dédiée ou une pagination avec DTO projection pour un affichage complet.
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations
                .stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupération d’une réservation par son identifiant.
     * @param id l'identifiant de la réservation
     * @return le DTO de la réservation correspondante
     */
    @Transactional(readOnly = true)
    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Réservation non trouvée pour l'id " + id));

        // 1. Recuperer le DTO (les points d'itinéraire ne sont pas dans la Table Reservation)
        ReservationDTO dto = reservationMapper.toDto(reservation);
        List<ItineraryPoint> points = itineraryPointRepository.findByReservationId(id);

        // 2. Enrichir le DTO avec les points d'itinéraire
        dto.setItineraryPoints(
                points.stream()
                        .map(itineraryPointMapper::toDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    /**
     * Récupération des réservations par identifiant de conducteur.
     * Retourne uniquement les DTO de réservation (sans points d’itinéraire) pour des raisons de performance.
     * @param driverId l'identifiant du conducteur
     * @return la liste des DTO de réservations du conducteur
     */
    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservationsByDriverId(Long driverId) {
        List<Reservation> reservations = reservationRepository.findByDriverIdOrderByStartDateDesc(driverId);
        return reservations
                .stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Création et persistance d’une nouvelle réservation à partir du DTO.
     * Création synchrone des itineraryPoints associés selon le type de réservation
     * @param dto le DTO à convertir en entité et à persister
     * @return le DTO de la réservation créée
     */
    @Transactional
    public ReservationDTO createReservation(ReservationDTO dto) {

        /// 1. Validation des dates //////////////////////////////////////////////////////////////////////////////
        if (dto.getStartDate().isAfter(dto.getEndDate())) { throw new IllegalArgumentException("La date de début doit être avant la date de fin."); }

        /// 2. Conversion du DTO en entité pour enregistrement et recuperation du statut pour evaluation /////////
        Reservation reservation = reservationMapper.toEntity(dto);
        Status requestedStatus = dto.getReservationStatus();

        /// 3. Création de la réservation ////////////////////////////////////////////////////////////////////////
        /// 3.1 Si on cree une Indisponibilité de reservation, les reservations en cours ou en attente doivent être annulées ///
        if (requestedStatus == Status.UNAVAILABLE) {
            // Récupération des réservations conflictuelles pour les annuler.
            List<Reservation> conflictingReservations = getConflictingReservations(dto);
            if (!conflictingReservations.isEmpty()) {
                // On met à jour les réservations en conflit avec le statut "CANCELLED"
                for (Reservation conflict : conflictingReservations) {
                    conflict.setReservationStatus(Status.CANCELLED);
                }
                reservationRepository.saveAll(conflictingReservations);
                // TODO: Envoyer une notification aux utilisateurs (et covoitureurs dans un second temps) dont la réservation est annulée.
            }
        /// 3.2. Sinon on crée une demande de réservation standard ///////////////////////////////////////////////
        } else {
            /// Vérification de la disponibilité du vehicule sur la plage de dates
            validateConflictingReservations(dto);
            // TODO: Gerer l'affichage de l'erreur.
        }
        /// Sauvegarde en BDD de la nouvelle réservation /////////////////////////////////////////////////////////
        Reservation savedReservation = reservationRepository.save(reservation);

        /// 4. Construction du DTO de retour complet /////////////////////////////////////////////////////////////
        return reservationMapper.toDto(savedReservation);
    }


    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationDTO dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Réservation non trouvée pour l'id " + id));

        /// On n'autorise la mise à jour du Status et du Conducteur uniquement
        reservation.setReservationStatus(dto.getReservationStatus());
        reservation.setDriver(idToEntityMapper.longToDriver(dto.getDriverId()));

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


    /**
     * Trouve les réservations pour un véhicule donné qui chevauchent une période donnée
     * @param reservation la reservation pour le véhicule dont on souhaite chercher les réservations en conflit
     * @return la liste des réservations du véhicule sur la période
     */
    private List<Reservation> getConflictingReservations(ReservationDTO reservation) {
        return reservationRepository.findByVehicleIdAndDates(
                reservation.getVehicleId(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );
    }


    /**
     * Vérifie que le véhicule n'est pas déjà réservé sur cette période.
     * @param reservation la reservation pour le véhicule dont on souhaite chercher les réservations en conflit
     * @throws IllegalStateException l'exception levée si le véhicule est déjà réservé sur cette période
     */
    private void validateConflictingReservations(ReservationDTO reservation) {
        List<Reservation> conflictingReservations = getConflictingReservations(reservation);
        if (!conflictingReservations.isEmpty()) {
            throw new IllegalStateException("Le véhicule est déjà réservé sur cette période.");
        }
    }

    /**
     * Trouve les réservations compatibles avec une réservation donnée.
     * Une réservation est compatible si :
     *  - soit (depart cherché = DEPART existant ET dateTime de départ cherché est le JOUR de DEPART existant),
     *  - soit (depart cherché = DESTINATION existante ET dateTime de départ cherché est le JOUR de RETOUR existant).
     *
     * @param reservation la réservation de référence pour trouver des réservations compatibles
     * @return la liste des DTO de réservations compatibles
     */
    public List<ReservationDTO> getCompatibleReservations(ReservationDTO reservation) {
        Long departureRequestedPlaceId = reservation.getDepartureId();
        LocalDateTime depStartOfDay = reservation.getStartDate().toLocalDate().atStartOfDay();
        LocalDateTime depEndOfDay   = reservation.getStartDate().toLocalDate().plusDays(1).atStartOfDay();

        // Récupérer les entités
        List<Reservation> compatibles =  reservationRepository.findCompatibleReservationsOnStartDateAndPlace(
                departureRequestedPlaceId,
                depStartOfDay,
                depEndOfDay
            );
        // Mapper vers DTO (pour alignement avec les autres methodes)
        return compatibles.stream()
                .map(reservationMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
        }
}
