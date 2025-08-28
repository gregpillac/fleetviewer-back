package com.eni.fleetviewer.back;


import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.mapper.VehicleMapper;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Vehicle;
import com.eni.fleetviewer.back.repository.VehicleRepository;
import com.eni.fleetviewer.back.service.VehicleService;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class VehicleServiceTest {


    @Mock
    private VehicleRepository vehicleRepository;


    @Mock
    private VehicleMapper vehicleMapper;


    @InjectMocks
    private VehicleService vehicleService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testAddVehicle_shouldReturnSavedVehicleDTO() {
        VehicleDTO inputDTO = new VehicleDTO(null, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, 1L);
        Place place = new Place(); place.setId(1L);
        Vehicle entity = new Vehicle(null, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, place);
        Vehicle saved = new Vehicle(1L, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, place);
        VehicleDTO expectedDTO = new VehicleDTO(1L, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, 1L);


        when(vehicleMapper.toEntity(inputDTO)).thenReturn(entity);
        when(vehicleRepository.save(entity)).thenReturn(saved);
        when(vehicleMapper.toDto(saved)).thenReturn(expectedDTO);


        VehicleDTO result = vehicleService.addVehicle(inputDTO);


        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Peugeot", result.getBrand());
    }


    @Test
    public void testGetAllVehicles_shouldReturnVehicleList() {
        Place place = new Place(); place.setId(1L);
        Vehicle vehicle = new Vehicle(1L, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, place);
        VehicleDTO dto = new VehicleDTO(1L, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, 1L);


        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDto(vehicle)).thenReturn(dto);


        List<VehicleDTO> result = vehicleService.getAllVehicles();


        assertEquals(1, result.size());
        assertEquals("208", result.get(0).getModel());
    }


    @Test
    public void testGetVehicleById_shouldReturnVehicleDTO() {
        Long id = 1L;
        Place place = new Place(); place.setId(1L);
        Vehicle vehicle = new Vehicle(id, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, place);
        VehicleDTO dto = new VehicleDTO(id, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, 1L);


        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toDto(vehicle)).thenReturn(dto);


        VehicleDTO result = vehicleService.getVehicleById(id);


        assertNotNull(result);
        assertEquals("AB-123-CD", result.getLicensePlate());
    }


    @Test
    public void testDeleteVehicleById_shouldCallRepositoryDelete() {
        Long id = 1L;
        when(vehicleRepository.existsById(id)).thenReturn(true);


        vehicleService.deleteVehicleById(id);


        verify(vehicleRepository).deleteById(id);
    }


    @Test
    public void testUpdateVehicle_shouldReturnUpdatedDTO() {
        Long id = 1L;
        Place place = new Place(); place.setId(1L);
        Vehicle existing = new Vehicle(id, "AB-123-CD", "Peugeot", "208", 4, 50000L, true, true, place);
        VehicleDTO inputDTO = new VehicleDTO(id, "AB-123-CD", "Peugeot", "208", 5, 52000L, false, false, 1L);
        Vehicle updated = new Vehicle(id, "AB-123-CD", "Peugeot", "208", 5, 52000L, false, false, place);
        VehicleDTO expectedDTO = new VehicleDTO(id, "AB-123-CD", "Peugeot", "208", 5, 52000L, false, false, 1L);


        when(vehicleRepository.findById(id)).thenReturn(Optional.of(existing));
        when(vehicleMapper.longToPlace(1L)).thenReturn(place);
        when(vehicleRepository.save(existing)).thenReturn(updated);
        when(vehicleMapper.toDto(updated)).thenReturn(expectedDTO);


        VehicleDTO result = vehicleService.updateVehicle(id, inputDTO);


        assertNotNull(result);
        assertEquals(52000L, result.getMileage());
        assertFalse(result.getIsInsuranceValid());
    }


    @Test
    public void testGetVehicleById_shouldThrowExceptionIfNotFound() {
        Long id = 99L;
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(RessourceNotFoundException.class, () -> vehicleService.getVehicleById(id));
    }
}