package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.RoleDTO;
import com.eni.fleetviewer.back.mapper.RoleMapper;
import com.eni.fleetviewer.back.repository.RoleRepository;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Retourne tous les rôles.
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retourne un rôle par son ID.
     */
    @Transactional(readOnly = true)
    public RoleDTO getRoleById(String id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDto)
                .orElseThrow(() -> new RessourceNotFoundException("Rôle introuvable pour l'id : " + id));
    }
}