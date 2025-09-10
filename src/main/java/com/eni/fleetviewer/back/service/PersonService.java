package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.mapper.AddressMapper;
import com.eni.fleetviewer.back.mapper.PersonMapper;
import com.eni.fleetviewer.back.mapper.PlaceMapper;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepo;
    private final PersonMapper personMapper;
    private final AddressMapper addressMapper;
    private final PlaceMapper placeMapper;

    @Transactional(readOnly = true)
    public List<PersonDTO> getPersons() {
        return personRepo.findAll()
                .stream()
                .map(personMapper::toDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByPlaceName(String placeName) {
        return personRepo.findByPlaceName(placeName)
                .stream()
                .map(personMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonDTO getPersonById(Long id) {
        Person p = personRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne introuvable"));
        return personMapper.toDto(p);
    }

    @Transactional
    public PersonDTO createPerson(PersonDTO dto) {
        Person p = personMapper.toEntity(dto);
        Person saved = personRepo.save(p);
        return personMapper.toDto(saved);
    }

    @Transactional
    public PersonDTO updatePerson(Long id, PersonDTO dto) {
        Person p = personRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne introuvable"));

        // champs autorisés (identique à ton controller initial)
        p.setFirstName(dto.getFirstName());
        p.setLastName(dto.getLastName());
        p.setEmail(dto.getEmail());
        p.setPhone(dto.getPhone());
        if (dto.getAddress() != null) p.setAddress(addressMapper.toEntity(dto.getAddress()));
        if (dto.getPlace() != null)   p.setPlace(placeMapper.toEntity(dto.getPlace()));

        Person saved = personRepo.save(p);
        return personMapper.toDto(saved);
    }

    @Transactional
    public void deletePerson(Long id) {
        if (!personRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne introuvable");
        }
        personRepo.deleteById(id);
    }
}
