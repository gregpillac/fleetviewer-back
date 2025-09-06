package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.ItineraryPointDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.ItineraryPointMapper;
import com.eni.fleetviewer.back.model.ItineraryPoint;
import com.eni.fleetviewer.back.model.id.ItineraryPointId;
import com.eni.fleetviewer.back.repository.ItineraryPointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItineraryPointService {

    private final ItineraryPointRepository repository;
    private final ItineraryPointMapper mapper;

    @Autowired
    public ItineraryPointService(ItineraryPointRepository repository, ItineraryPointMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ItineraryPointDTO create(ItineraryPointDTO dto) {
        ItineraryPoint entity = mapper.toEntity(dto);
        ItineraryPoint saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Transactional
    public ItineraryPointDTO update(Long reservationId, Long placeId, ItineraryPointDTO dto) {
        ItineraryPointId id = new ItineraryPointId(reservationId, placeId);
        ItineraryPoint existing = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Point d’itinéraire introuvable pour la réservation " + reservationId + " et le lieu " + placeId));
        existing.setDateTime(dto.getDateTime());

        ItineraryPoint updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Transactional
    public void deleteItineraryPoint(Long reservationId, Long placeId) {
        ItineraryPointId id = new ItineraryPointId(reservationId, placeId);
        ItineraryPoint existing = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Point d’itinéraire introuvable pour la réservation " + reservationId + " et le lieu " + placeId));
        repository.delete(existing);
    }

    @Transactional
    public ItineraryPointDTO getById(Long reservationId, Long placeId) {
        ItineraryPointId id = new ItineraryPointId(reservationId, placeId);
        ItineraryPoint entity = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Point d’itinéraire introuvable pour la réservation " + reservationId + " et le lieu " + placeId));
        return mapper.toDto(entity);
    }

    @Transactional
    public List<ItineraryPointDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}