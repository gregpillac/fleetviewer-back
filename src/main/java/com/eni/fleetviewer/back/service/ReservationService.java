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
import com.eni.fleetviewer.back.model.ReservationStatus;
import com.eni.fleetviewer.back.repository.ItineraryPointRepository;
import com.eni.fleetviewer.back.repository.ReservationRepository;
import com.eni.fleetviewer.back.repository.ReservationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final IdToEntityMapper idToEntityMapper;
    private final ReservationStatusRepository reservationStatusRepository;
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
     * Création et persistance d’une nouvelle réservation à partir du DTO.
     * Création synchrone des itineraryPoints associés selon le type de réservation
     * @param dto le DTO à convertir en entité et à persister
     * @return le DTO de la réservation créée
     */
    @Transactional
    public ReservationDTO createReservation(ReservationDTO dto) {

        /// 0. Validation des dates ///////////////////////////////////////////////////////////////////
        if (dto.getStartDate().isAfter(dto.getEndDate())) { throw new IllegalArgumentException("La date de début doit être avant la date de fin."); }

        /// 3. Conversion du DTO en entité pour manipulation et enregistrement, recuperation du statut pour evaluation /////////////
        Reservation reservation = reservationMapper.toEntity(dto);
        ReservationStatus requestedStatus = idToEntityMapper.longToReservationStatus(dto.getReservationStatusId());
        Reservation savedReservation = new Reservation();

        /// 4. Création de la réservation //////////////////////////////////////////////////////////////////////////////////////////

        /// 4.1 Si on cree une indisponibilité de reservation, les reservations en cours ou en attente doivent être annulées ///////////////
        if (requestedStatus.getName().equalsIgnoreCase(Status.UNAVAILABLE.toString())) {

            // Récupération des réservations conflictuelles pour les annuler.
            List<Reservation> conflictingReservations = getConflictingReservations(dto);
            if (!conflictingReservations.isEmpty()) {
                // On met à jour les réservations en conflit avec le statut "CANCELLED"
                ReservationStatus cancelledStatus = reservationStatusRepository.findByName(Status.CANCELLED.toString())
                        .orElseThrow(() -> new IllegalStateException("Le statut 'Annulée' est introuvable en base de données."));
                for (Reservation conflict : conflictingReservations) {
                    conflict.setReservationStatus(cancelledStatus);
                }
                reservationRepository.saveAll(conflictingReservations);
                // TODO: Envoyer une notification aux utilisateurs (et covoitureurs dans un second temps) dont la réservation est annulée.
            }

        /// 4.2. Sinon on crée une demande de réservation standard ///////////////////////////////////////////////
        } else {
            /// Vérification de la disponibilité du vehicule sur la plage de dates
            validateConflictingReservations(dto);
            /// Vérification des points d'itinéraire (au moins un départ et une arrivée) //////////////////////////////////
            validateItineraryPoints(dto.getItineraryPoints());
            /// Définition du status par défaut à 'PENDING' (En attente') /////////////////////////////////////////////////
            ReservationStatus pendingStatus = reservationStatusRepository.findByName(Status.PENDING.getStatus())
                    .orElseThrow(() -> new IllegalStateException("Le statut par défaut 'En attente' n'a pas été trouvé en base de données."));
            reservation.setReservationStatus(pendingStatus);
            ///Sauvegarde en BDD de la nouvelle réservation //////////////////////////////////////////////////////////////
            savedReservation = reservationRepository.save(reservation);

            /// 5. Création et sauvegarde des points d'itinéraire associés à la réservation //////////////////////////////
            // TODO: DEPORTER cette logique dans un itineraryPointService
            Long savedReservationId = savedReservation.getId();
            List<ItineraryPoint> itineraryPoints = dto.getItineraryPoints().stream()
                    .map(itineraryPoint -> {
                        // On assigne l'ID de la réservation qui vient d'être créée au point d itinerarire
                        itineraryPoint.setReservationId(savedReservationId);
                        return itineraryPointMapper.toEntity(itineraryPoint);
                    })
                    .collect(Collectors.toList());

            itineraryPointRepository.saveAll(itineraryPoints);
        }

        /// 6. Construction du DTO de retour complet ////////////////////////////////////////////////
        // On rappelle getReservationById pour être sûr d'avoir l'état le plus à jour et pour inclure les points d'itinéraire dans la réponse(NON).
        return getReservationById(savedReservation.getId());
    }


    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Réservation non trouvée pour l'id " + id));

        /// On n'autorise la mise à jour du Status et du Conducteur uniquement
        reservation.setReservationStatus(idToEntityMapper.longToReservationStatus(reservationDTO.getReservationStatusId()));
        reservation.setDriver(idToEntityMapper.longToDriver(reservationDTO.getDriverId()));

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
     * Valide la liste des points points pour une reservation. On s'assure que :
     * 1. La liste n'est pas nulle.
     * 2. Qu'elle contient un point de depart et d'arrivée.
     * @param points la liste des points à valider.
     * @throws IllegalArgumentException si la liste est nulle et n'a pas exactement 1 depart et une arrivée.
     */
    private void validateItineraryPoints(List<ItineraryPointDTO> points) {
        if (points == null) {
            throw new IllegalArgumentException("La liste des points d'itinéraire ne peut pas être nulle.");
        }
        long departureCount = points.stream()
                .filter(p -> "départ".equalsIgnoreCase(p.getPointType()))
                .count();
        long arrivalCount = points.stream()
                .filter(p -> "arrivée".equalsIgnoreCase(p.getPointType()))
                .count();
        if (departureCount != 1 || arrivalCount != 1) {
            throw new IllegalArgumentException("Une réservation doit contenir exactement un point de 'départ' et un point d''arrivée'.");
        }
    }

    /**
     * Trouve les réservations pour un véhicule donné qui chevauchent une période donnée
     * @param reservation la reservation pour le véhicule dont on souhaite charcher les réservations
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
     * Valide la liste des points pour une reservation. On s'assure que :
     */
    private void validateConflictingReservations(ReservationDTO reservation) {
        List<Reservation> conflictingReservations = getConflictingReservations(reservation);

        if (!conflictingReservations.isEmpty()) {
            throw new IllegalStateException("Le véhicule est déjà réservé sur cette période.");
        }
    }
}
