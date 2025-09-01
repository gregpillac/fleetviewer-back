package com.eni.fleetviewer.back;

import com.eni.fleetviewer.back.dto.RoleDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.RoleMapper;
import com.eni.fleetviewer.back.model.Role;
import com.eni.fleetviewer.back.repository.RoleRepository;
import com.eni.fleetviewer.back.service.RoleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour RoleService.
 */
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRoles_shouldReturnListOfRoleDTO() {
        Role role1 = new Role("ADMIN", "Administrateur");
        Role role2 = new Role("USER", "Utilisateur");

        RoleDTO dto1 = new RoleDTO("ADMIN", "Administrateur");
        RoleDTO dto2 = new RoleDTO("USER", "Utilisateur");

        when(roleRepository.findAll()).thenReturn(List.of(role1, role2));
        when(roleMapper.toDto(role1)).thenReturn(dto1);
        when(roleMapper.toDto(role2)).thenReturn(dto2);

        List<RoleDTO> result = roleService.getAllRoles();

        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getId());
        assertEquals("Utilisateur", result.get(1).getDescription());

        verify(roleRepository).findAll();
        verify(roleMapper).toDto(role1);
        verify(roleMapper).toDto(role2);
    }

    @Test
    public void testGetRoleById_shouldReturnRoleDTO() {
        Role role = new Role("ADMIN", "Administrateur");
        RoleDTO dto = new RoleDTO("ADMIN", "Administrateur");

        when(roleRepository.findById("ADMIN")).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(dto);

        RoleDTO result = roleService.getRoleById("ADMIN");

        assertNotNull(result);
        assertEquals("ADMIN", result.getId());
        assertEquals("Administrateur", result.getDescription());

        verify(roleRepository).findById("ADMIN");
        verify(roleMapper).toDto(role);
    }

    @Test
    public void testGetRoleById_shouldThrowExceptionWhenNotFound() {
        when(roleRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> {
            roleService.getRoleById("UNKNOWN");
        });

        verify(roleRepository).findById("UNKNOWN");
        verifyNoInteractions(roleMapper);
    }
}