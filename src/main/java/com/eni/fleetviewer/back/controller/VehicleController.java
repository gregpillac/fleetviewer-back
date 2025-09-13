package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }


    /**
     * GET /api/vehicles : Récupère la liste de tous les véhicules.
     * @return ResponseEntity avec le statut 200 OK et la liste des véhicules dans le corps.
     */
    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {

        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    /**
     * GET /api/vehicles/{id} : Récupère un véhicule par son identifiant.
     * @param id L'identifiant du véhicule à récupérer.
     * @return ResponseEntity avec le statut 200 OK et le véhicule trouvé,
     * ou 404 Not Found si l'ID n'existe pas (géré par un exception handler).
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById (
            @PathVariable Long id) {

        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/place/{placeName}")
    public List<VehicleDTO> getVehiclesByPlaceName(@PathVariable String placeName) {
        return vehicleService.getVehiclesByPlaceName(placeName);
    }

    /**
     * POST /api/vehicles : Crée un nouveau véhicule.
     * @param dto Le DTO du véhicule à créer, validé à partir du corps de la requête.
     * @return ResponseEntity avec le statut 201 Created, l'en-tête Location pointant
     * vers la nouvelle ressource, et le DTO du véhicule créé dans le corps.
     */
    @PostMapping
    public ResponseEntity<VehicleDTO> addVehicle(
            @Valid @RequestBody VehicleDTO dto){

        VehicleDTO created = vehicleService.addVehicle(dto);
        // Création de l'URI pour l'en-tête Location
        URI location = URI.create("/api/vehicles/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }


    /**
     * PUT /api/vehicles/{id} : Met à jour un véhicule existant.
     * @param id L'identifiant du véhicule à mettre à jour.
     * @param dto Le DTO contenant les nouvelles informations du véhicule.
     * @return ResponseEntity avec le statut 200 OK et le DTO du véhicule mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleDTO dto) {

        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, dto);
        return ResponseEntity.ok(updatedVehicle);
    }


    /**
     * DELETE /api/vehicles/{id} : Supprime un véhicule par son identifiant.
     * @param id L'identifiant du véhicule à supprimer.
     * @return ResponseEntity avec le statut 204 No Content si la suppression a réussi.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleById (
            @PathVariable Long id) {

        vehicleService.deleteVehicleById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/vehicles/byPlace{placeIdbyPlace{placeId} : Récupère une liste de véhicules par un identifiant du site de rattachement.
     * @param placeId L'identifiant du site des vehicules à récupérer.
     * @return ResponseEntity avec le statut 200 OK et le véhicule trouvé,
     * ou 404 Not Found si l'ID n'existe pas (géré par un exception handler).
     */
    @GetMapping("/byPlace{placeId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByPlaceId (
            @PathVariable Long placeId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByPlaceId(placeId);
        return ResponseEntity.ok(vehicles);
    }

    // GET /api/reservations/available-vehicles?start=...&end=...[&placeId=...]
    @GetMapping("/available-vehicles")
    public ResponseEntity<List<VehicleDTO>> getAvailable(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Long placeId) {
        return ResponseEntity.ok(vehicleService.getAvailableVehicles(start, end, placeId));
    }
}