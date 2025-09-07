package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.VehicleKeyDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.VehicleKeyMapper;
import com.eni.fleetviewer.back.model.VehicleKey;
import com.eni.fleetviewer.back.repository.VehicleKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleKeyService {

    private final VehicleKeyRepository vehicleKeyRepository;
    private final VehicleKeyMapper vehicleKeyMapper;

    @Transactional(readOnly = true)
    public List<VehicleKeyDTO> getAllKeys() {
        return vehicleKeyRepository.findAll()
                .stream()
                .map(vehicleKeyMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleKeyDTO get(Long id) {
        VehicleKey vk = vehicleKeyRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Clé introuvable id=" + id));
        return vehicleKeyMapper.toDto(vk);
    }

    public VehicleKeyDTO create(VehicleKeyDTO dto) {
        VehicleKey entity = vehicleKeyMapper.toEntity(dto);
        entity.setId(null); // sécurité
        entity = vehicleKeyRepository.save(entity);
        return vehicleKeyMapper.toDto(entity);
    }

    public VehicleKeyDTO update(Long id, VehicleKeyDTO dto) {
        if (!vehicleKeyRepository.existsById(id)) {
            throw new RessourceNotFoundException("Clé introuvable id=" + id);
        }
        VehicleKey entity = vehicleKeyMapper.toEntity(dto);
        entity.setId(id);
        entity = vehicleKeyRepository.save(entity);
        return vehicleKeyMapper.toDto(entity);
    }

    public void delete(Long id) {
        if (!vehicleKeyRepository.existsById(id)) {
            throw new RessourceNotFoundException("Clé introuvable id=" + id);
        }
        vehicleKeyRepository.deleteById(id);
    }

    // Filtres utiles
    @Transactional(readOnly = true)
    public List<VehicleKeyDTO> getByVehicle(Long vehicleId) {
        return vehicleKeyRepository.findByVehicle_Id(vehicleId).stream().map(vehicleKeyMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<VehicleKeyDTO> getByPlace(Long placeId) {
        return vehicleKeyRepository.findByPlace_Id(placeId).stream().map(vehicleKeyMapper::toDto).toList();
    }
}
