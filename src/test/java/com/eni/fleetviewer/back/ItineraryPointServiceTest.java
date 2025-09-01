package com.eni.fleetviewer.back;

import com.eni.fleetviewer.back.dto.ItineraryPointDTO;
import com.eni.fleetviewer.back.mapper.ItineraryPointMapper;
import com.eni.fleetviewer.back.model.ItineraryPoint;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Reservation;
import com.eni.fleetviewer.back.model.id.ItineraryPointId;
import com.eni.fleetviewer.back.repository.ItineraryPointRepository;
import com.eni.fleetviewer.back.service.ItineraryPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test unitaire du service ItineraryPointService
 */
public class ItineraryPointServiceTest {

    @Mock
    private ItineraryPointRepository repository;

    @Mock
    private ItineraryPointMapper mapper;

    @InjectMocks
    private ItineraryPointService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate_shouldReturnSavedDTO() {
        ItineraryPointDTO inputDTO = new ItineraryPointDTO(1L, 2L, LocalDateTime.now(), "ARRIVAL");
        ItineraryPoint entity = new ItineraryPoint();
        ItineraryPoint savedEntity = new ItineraryPoint();
        ItineraryPointDTO expectedDTO = new ItineraryPointDTO(1L, 2L, inputDTO.getDateTime(), "ARRIVAL");

        when(mapper.toEntity(inputDTO)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(expectedDTO);

        ItineraryPointDTO result = service.create(inputDTO);

        assertNotNull(result);
        assertEquals(expectedDTO.getReservationId(), result.getReservationId());
        verify(mapper).toEntity(inputDTO);
        verify(repository).save(entity);
        verify(mapper).toDto(savedEntity);
    }

    @Test
    public void testGetById_shouldReturnDTO() {
        Long resId = 1L;
        Long placeId = 2L;
        ItineraryPointId id = new ItineraryPointId(resId, placeId);
        ItineraryPoint entity = new ItineraryPoint();
        ItineraryPointDTO dto = new ItineraryPointDTO(resId, placeId, LocalDateTime.now(), "DEPARTURE");

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        ItineraryPointDTO result = service.getById(resId, placeId);

        assertNotNull(result);
        assertEquals("DEPARTURE", result.getPointType());
        verify(repository).findById(id);
        verify(mapper).toDto(entity);
    }

    @Test
    public void testUpdate_shouldReturnUpdatedDTO() {
        Long resId = 1L;
        Long placeId = 2L;
        ItineraryPointId id = new ItineraryPointId(resId, placeId);

        Reservation reservation = new Reservation();
        reservation.setId(resId);
        Place place = new Place();
        place.setId(placeId);

        ItineraryPoint existing = new ItineraryPoint(reservation, place, LocalDateTime.now(), "ARRIVAL");

        ItineraryPointDTO inputDTO = new ItineraryPointDTO(resId, placeId, LocalDateTime.now().plusHours(2), "DEPARTURE");
        ItineraryPoint updated = new ItineraryPoint(reservation, place, inputDTO.getDateTime(), "DEPARTURE");
        ItineraryPointDTO expectedDTO = new ItineraryPointDTO(resId, placeId, inputDTO.getDateTime(), "DEPARTURE");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(expectedDTO);

        ItineraryPointDTO result = service.update(resId, placeId, inputDTO);

        assertNotNull(result);
        assertEquals("DEPARTURE", result.getPointType());
        verify(repository).findById(id);
        verify(repository).save(existing);
        verify(mapper).toDto(updated);
    }

    @Test
    public void testDelete_shouldCallRepositoryDelete() {
        Long resId = 1L;
        Long placeId = 2L;
        ItineraryPointId id = new ItineraryPointId(resId, placeId);

        Reservation res = new Reservation();
        res.setId(resId);
        Place place = new Place();
        place.setId(placeId);
        ItineraryPoint point = new ItineraryPoint(res, place, LocalDateTime.now(), "ARRIVAL");

        when(repository.findById(id)).thenReturn(Optional.of(point));

        service.deleteItineraryPoint(resId, placeId);

        verify(repository).delete(point);
    }

    @Test
    public void testGetAll_shouldReturnDTOList() {
        ItineraryPoint entity1 = new ItineraryPoint();
        ItineraryPoint entity2 = new ItineraryPoint();

        ItineraryPointDTO dto1 = new ItineraryPointDTO(1L, 2L, LocalDateTime.now(), "DEPARTURE");
        ItineraryPointDTO dto2 = new ItineraryPointDTO(3L, 4L, LocalDateTime.now(), "ARRIVAL");

        when(repository.findAll()).thenReturn(List.of(entity1, entity2));
        when(mapper.toDto(entity1)).thenReturn(dto1);
        when(mapper.toDto(entity2)).thenReturn(dto2);

        List<ItineraryPointDTO> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
        verify(mapper).toDto(entity1);
        verify(mapper).toDto(entity2);
    }
}