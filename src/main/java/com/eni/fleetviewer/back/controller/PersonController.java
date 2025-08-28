package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.mapper.AddressMapper;
import com.eni.fleetviewer.back.mapper.PersonMapper;
import com.eni.fleetviewer.back.mapper.PlaceMapper;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepo;
    private final PersonMapper personMapper;
    private final AddressMapper addressMapper;
    private final PlaceMapper placeMapper;

    @GetMapping
    public List<PersonDTO> getPersons() {
        return personRepo.findAll().stream().map(personMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public PersonDTO getPersonById(@PathVariable Long id) {
        Person p = personRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne introuvable"));
        return personMapper.toDto(p);
    }

    @PostMapping
    public PersonDTO createPerson(@RequestBody PersonDTO dto) {
        Person p = personMapper.toEntity(dto);
        Person savedPerson = personRepo.save(p);
        return personMapper.toDto(savedPerson);
    }

    @PutMapping("/{id}")
    public PersonDTO updatePerson(@PathVariable Long id, @RequestBody PersonDTO dto) {
        Person p = personRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne introuvable"));

        // copie des champs autorisés
        p.setFirstName(dto.getFirstName());
        p.setLastName(dto.getLastName());
        p.setEmail(dto.getEmail());
        p.setPhone(dto.getPhone());
        if (dto.getAddress() != null) p.setAddress(addressMapper.toEntity(dto.getAddress()));
        if (dto.getPlace() != null) p.setPlace(placeMapper.toEntity(dto.getPlace()));

        Person savedPerson = personRepo.save(p);
        return personMapper.toDto(savedPerson);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        personRepo.deleteById(id);
    }
}