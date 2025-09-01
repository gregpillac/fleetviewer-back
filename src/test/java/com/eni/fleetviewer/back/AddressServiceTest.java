package com.eni.fleetviewer.back;

import com.eni.fleetviewer.back.dto.AddressDTO;
import com.eni.fleetviewer.back.mapper.AddressMapper;
import com.eni.fleetviewer.back.model.Address;
import com.eni.fleetviewer.back.repository.AddressRepository;
import com.eni.fleetviewer.back.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test unitaire de AddressService (méthodes create, getById, update, delete)
 */
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAddress_shouldReturnSavedAddressDTO() {
        AddressDTO inputDTO = new AddressDTO(null, "123 Rue A", "Bat B", "75000", "Paris", Map.of("lat","48.85","lng","2.35"));
        Address addressEntity = new Address(null, "123 Rue A", "Bat B", "75000", "Paris",Map.of("lat","48.85","lng","2.35"));
        Address savedAddress = new Address(1L, "123 Rue A", "Bat B", "75000", "Paris",Map.of("lat","48.85","lng","2.35"));
        AddressDTO expectedDTO = new AddressDTO(1L, "123 Rue A", "Bat B", "75000", "Paris",Map.of("lat","48.85","lng","2.35"));

        when(addressMapper.addressDTOToAddress(inputDTO)).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(savedAddress);
        when(addressMapper.addressToAddressDTO(savedAddress)).thenReturn(expectedDTO);

        AddressDTO result = addressService.createAddress(inputDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Paris", result.getCity());

        verify(addressMapper).addressDTOToAddress(inputDTO);
        verify(addressRepository).save(addressEntity);
        verify(addressMapper).addressToAddressDTO(savedAddress);
    }

    @Test
    public void testGetAddressById_shouldReturnAddressDTO() {
        Long id = 1L;
        Address addressEntity = new Address(id, "123 Rue A", "Bat B", "75000", "Paris", null);
        AddressDTO expectedDTO = new AddressDTO(id, "123 Rue A", "Bat B", "75000", "Paris", null);

        when(addressRepository.findById(id)).thenReturn(Optional.of(addressEntity));
        when(addressMapper.addressToAddressDTO(addressEntity)).thenReturn(expectedDTO);

        AddressDTO result = addressService.getAddressById(id);

        assertNotNull(result);
        assertEquals("123 Rue A", result.getAddressFirstLine());
        assertEquals("Paris", result.getCity());

        verify(addressRepository).findById(id);
        verify(addressMapper).addressToAddressDTO(addressEntity);
    }

    @Test
    public void testUpdateAddress_shouldReturnUpdatedDTO() {
        Long id = 1L;
        AddressDTO inputDTO = new AddressDTO(id, "456 Rue C", "Etage 3", "69000", "Lyon", null);
        Address existingAddress = new Address(id, "Old Line", "Old Line 2", "00000", "Old City", null);
        Address updatedAddress = new Address(id, "456 Rue C", "Etage 3", "69000", "Lyon", null);
        AddressDTO expectedDTO = new AddressDTO(id, "456 Rue C", "Etage 3", "69000", "Lyon", null);

        when(addressRepository.findById(id)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(existingAddress)).thenReturn(updatedAddress);
        when(addressMapper.addressToAddressDTO(updatedAddress)).thenReturn(expectedDTO);

        AddressDTO result = addressService.updateAddress(id, inputDTO);

        assertNotNull(result);
        assertEquals("Lyon", result.getCity());

        verify(addressRepository).findById(id);
        verify(addressRepository).save(existingAddress);
        verify(addressMapper).addressToAddressDTO(updatedAddress);
    }

    @Test
    public void testDeleteAddress_shouldCallRepositoryDeleteById() {
        Long id = 1L;

        // Mock : on indique que l'adresse existe
        when(addressRepository.existsById(id)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(id);

        // Appel de la méthode
        addressService.deleteAddress(id);

        // Vérifications
        verify(addressRepository).existsById(id);
        verify(addressRepository).deleteById(id);
    }
}