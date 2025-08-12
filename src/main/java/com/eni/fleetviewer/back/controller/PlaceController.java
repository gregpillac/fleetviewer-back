package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.PlaceDTO;
import com.eni.fleetviewer.back.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    /**
     * GET /api/places : Récupère la liste de tous les lieux.
     * @return ResponseEntity avec le statut 200 OK et la liste des lieux dans le corps.
     */
    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getAllPlaces() {
        List<PlaceDTO> places = placeService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    /**
     * GET /api/places/{id} : Récupère un site par son identifiant.
     * @param id L'identifiant du site à récupérer.
     * @return ResponseEntity avec le statut 200 OK et le site trouvé,
     * ou 404 Not Found si l'ID n'existe pas (géré par un exception handler).
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> getPlaceById (
            @PathVariable Long id) {

        PlaceDTO place = placeService.getPlaceById(id);
        return ResponseEntity.ok(place);
    }


    /**
     * POST /api/places : Crée un nouveau site.
     * @param dto Le DTO du site à créer, validé à partir du corps de la requête.
     * @return ResponseEntity avec le statut 201 Created, l'en-tête Location pointant
     * vers la nouvelle ressource, et le DTO du site créé dans le corps.
     */
    @PostMapping
    public ResponseEntity<PlaceDTO> addPlace(
            @Valid @RequestBody PlaceDTO dto){

        PlaceDTO created = placeService.addPlace(dto);
        // Création de l'URI pour l'en-tête Location
        URI location = URI.create("/api/places/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }


    /**
     * PUT /api/places/{id} : Met à jour un site existant.
     * @param id L'identifiant du site à mettre à jour.
     * @param dto Le DTO contenant les nouvelles informations du site.
     * @return ResponseEntity avec le statut 200 OK et le DTO du site mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaceDTO> updatePlace(
            @PathVariable Long id,
            @Valid @RequestBody PlaceDTO dto) {

        PlaceDTO updatedPlace = placeService.updatePlace(id, dto);
        return ResponseEntity.ok(updatedPlace);
    }


    /**
     * DELETE /api/places/{id} : Supprime un site par son identifiant.
     * @param id L'identifiant du site à supprimer.
     * @return ResponseEntity avec le statut 204 No Content si la suppression a réussi.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaceById (
            @PathVariable Long id) {

        placeService.deletePlaceById(id);
        return ResponseEntity.noContent().build();
    }
}
