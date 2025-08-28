package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.AddressDTO;
import com.eni.fleetviewer.back.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour gérer les adresses.
 */
@RestController
@RequestMapping("/api/addresses") // URL de base : /api/addresses
public class AddressController {

    private final AddressService addressService;

    /**
     * Injection du service d'adresse.
     */
    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Endpoint POST pour créer une nouvelle adresse.
     * Exemple d'appel : POST /api/addresses avec un JSON dans le corps.
     *
     * @param addressDTO l'adresse à créer (validée automatiquement)
     * @return l'adresse créée avec un code HTTP 201
     */
    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO createdAddress = addressService.createAddress(addressDTO);
        return ResponseEntity.status(201).body(createdAddress); // HTTP 201 = Created
    }

    /**
     * Endpoint GET pour récupérer une adresse par son ID.
     * Exemple d'appel : GET /api/addresses/{id}
     *
     * @param id identifiant de l'adresse
     * @return l'adresse correspondante ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        AddressDTO address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(id, addressDTO);
        return ResponseEntity.ok(updatedAddress);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }

}