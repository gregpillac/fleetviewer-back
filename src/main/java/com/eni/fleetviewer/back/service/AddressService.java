package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.AddressDTO;
import com.eni.fleetviewer.back.mapper.AddressMapper;
import com.eni.fleetviewer.back.model.Address;
import com.eni.fleetviewer.back.repository.AddressRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Service métier pour gérer les opérations sur les adresses.
 */
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    /**
     * Injection des dépendances via le constructeur.
     */
    @Autowired
    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    /**
     * Crée une nouvelle adresse en base de données.
     *
     * @param addressDTO les données de l'adresse à créer
     * @return l'adresse créée, sous forme de DTO
     */
    @Transactional
    public AddressDTO createAddress(@Valid AddressDTO addressDTO) {
        Address address = addressMapper.toEntity(addressDTO);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    /**
     * Récupère une adresse par son ID.
     *
     * @param id identifiant de l'adresse
     * @return l'adresse trouvée, ou exception si non trouvée
     */
    @Transactional(readOnly = true)
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Adresse introuvable pour l'ID " + id));
        return addressMapper.toDto(address);
    }

    /**
     * Met à jour une adresse existante.
     *
     * @param id         identifiant de l'adresse à mettre à jour
     * @param addressDTO nouvelles données
     * @return l'adresse mise à jour, sous forme de DTO
     */
    @Transactional
    public AddressDTO updateAddress(Long id, @Valid AddressDTO addressDTO) {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Adresse introuvable pour l'ID " + id));

        // Met à jour les champs (sauf l'ID)
        existing.setAddressFirstLine(addressDTO.getAddressFirstLine());
        existing.setAddressSecondLine(addressDTO.getAddressSecondLine());
        existing.setPostalCode(addressDTO.getPostalCode());
        existing.setCity(addressDTO.getCity());
        existing.setGpsCoords(addressDTO.getGpsCoords());

        Address updated = addressRepository.save(existing);
        return addressMapper.toDto(updated);
    }

    /**
     * Supprime une adresse par son ID.
     *
     * @param id identifiant de l'adresse à supprimer
     */
    @Transactional
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new NoSuchElementException("Adresse introuvable pour l'ID " + id);
        }
        addressRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }
}