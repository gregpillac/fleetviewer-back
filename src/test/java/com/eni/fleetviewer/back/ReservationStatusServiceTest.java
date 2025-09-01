package com.eni.fleetviewer.back;

import com.eni.fleetviewer.back.dto.ReservationStatusDTO;
import com.eni.fleetviewer.back.mapper.ReservationStatusMapper;
import com.eni.fleetviewer.back.model.ReservationStatus;
import com.eni.fleetviewer.back.repository.ReservationStatusRepository;
import com.eni.fleetviewer.back.service.ReservationStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationStatusServiceTest {

    @Mock
    private ReservationStatusRepository statusRepository;

    @Mock
    private ReservationStatusMapper statusMapper;

    @InjectMocks
    private ReservationStatusService statusService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllStatuses_shouldReturnListOfDTOs() {
        // Données simulées
        ReservationStatus status1 = new ReservationStatus(1L, "En attente");
        ReservationStatus status2 = new ReservationStatus(2L, "Confirmée");

        ReservationStatusDTO dto1 = new ReservationStatusDTO(1L, "En attente");
        ReservationStatusDTO dto2 = new ReservationStatusDTO(2L, "Confirmée");

        List<ReservationStatus> entityList = Arrays.asList(status1, status2);
        List<ReservationStatusDTO> dtoList = Arrays.asList(dto1, dto2);

        // Mocks
        when(statusRepository.findAll()).thenReturn(entityList);
        when(statusMapper.toDto(status1)).thenReturn(dto1);
        when(statusMapper.toDto(status2)).thenReturn(dto2);

        // Appel
        List<ReservationStatusDTO> result = statusService.getAllStatuses();

        // Vérifications
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("En attente", result.get(0).getName());

        verify(statusRepository).findAll();
        verify(statusMapper).toDto(status1);
        verify(statusMapper).toDto(status2);
    }
}