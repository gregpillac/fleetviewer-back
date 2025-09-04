package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.PlaceDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.IdToEntityMapper;
import com.eni.fleetviewer.back.mapper.PlaceMapper;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepo;
    private final PlaceMapper placeMapper;
    private final IdToEntityMapper idToEntityMapper;


    /**
     * Récupération de tous les sites.
     * @return la liste des DTO de sites
     */
    @Transactional(readOnly = true)
    public List<PlaceDTO> getAllPlaces() {
        return placeRepo.findAll()
                .stream()
                .map(placeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupération d’un site par son identifiant.
     * @param id l'identifiant du site
     * @return le DTO du site correspondant
     */
    @Transactional(readOnly = true)
    public PlaceDTO getPlaceById(Long id) {
        Place place = placeRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Site non trouvé pour l'id " + id));
        return placeMapper.toDto(place);
    }

    /**
     * Suppression d’un site par son identifiant.
     * @param id l'identifiant du site à supprimer
     */
    @Transactional
    public void deletePlaceById(Long id) {
        if (!placeRepo.existsById(id)) {
            throw new RessourceNotFoundException("Site non trouvé pour l'id " + id);
        }
        placeRepo.deleteById(id);
    }

    /**
     * Création et persistance d’un nouveau site à partir du DTO.
     * @param dto le DTO à convertir en entité et à persister
     * @return le DTO du site créé
     */
    @Transactional
    public PlaceDTO addPlace(PlaceDTO dto) {
        // 1) Conversion du DTO en entité
        Place p = placeMapper.toEntity(dto);
        // 2) Sauvegarde en base
        Place saved = placeRepo.save(p);
        // 3) Retourne un DTO construit à partir de l’entité persistée
        return placeMapper.toDto(saved);
    }

/**
     * Mise à jour d’un site existant.
     * @param id l'identifiant du site à mettre à jour
     * @param dto le DTO contenant les nouvelles informations
     * @return le DTO du site mis à jour
     */
    @Transactional
    public PlaceDTO updatePlace(Long id, PlaceDTO dto) {
        Place existing = placeRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Site non trouvé pour l'id " + id));
        // Mise à jour des champs
        existing.setName(dto.getName());
        existing.setPublic(dto.isPublic());
        existing.setPlaceType(idToEntityMapper.longToPlaceType(dto.getPlaceTypeId()));
        existing.setAddress(idToEntityMapper.longToAddress(dto.getAddressId()));
        // Sauvegarde
        Place saved = placeRepo.save(existing);
        return placeMapper.toDto(saved);
    }
}