package com.eni.fleetviewer.back;

import com.eni.fleetviewer.back.dto.VehicleStatusDTO;
import com.eni.fleetviewer.back.mapper.VehicleStatusMapper;
import com.eni.fleetviewer.back.model.VehicleStatus;
import com.eni.fleetviewer.back.repository.VehicleStatusRepository;
import com.eni.fleetviewer.back.service.VehicleStatusService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleStatusServiceTest {

    private final VehicleStatusRepository repository = mock(VehicleStatusRepository.class);
    private final VehicleStatusMapper mapper = mock(VehicleStatusMapper.class);
    private final VehicleStatusService service = new VehicleStatusService(repository, mapper);

    @Test
    void testGetById_shouldReturnStatusDTO() {
        VehicleStatus entity = new VehicleStatus(1L, "Disponible");
        VehicleStatusDTO dto = new VehicleStatusDTO(1L, "Disponible");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        VehicleStatusDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Disponible", result.getName());
    }

    @Test
    void testGetAll_shouldReturnListOfDTOs() {
        VehicleStatus entity1 = new VehicleStatus(1L, "Disponible");
        VehicleStatus entity2 = new VehicleStatus(2L, "En maintenance");

        when(repository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(mapper.toDto(entity1)).thenReturn(new VehicleStatusDTO(1L, "Disponible"));
        when(mapper.toDto(entity2)).thenReturn(new VehicleStatusDTO(2L, "En maintenance"));

        var result = service.getAllStatuses();

        assertEquals(2, result.size());
        assertEquals("Disponible", result.get(0).getName());
        assertEquals("En maintenance", result.get(1).getName());
    }
}