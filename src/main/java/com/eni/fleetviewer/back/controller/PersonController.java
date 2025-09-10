package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<PersonDTO> getPersons() {
        return personService.getPersons();
    }

    @GetMapping("/place/{placeName}")
    public List<PersonDTO> getPersonsByPlaceName(@PathVariable String placeName) {
        return personService.getPersonsByPlaceName(placeName);
    }

    @GetMapping("/{id}")
    public PersonDTO getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping
    public PersonDTO createPerson(@RequestBody PersonDTO dto) {
        return personService.createPerson(dto);
    }

    @PutMapping("/{id}")
    public PersonDTO updatePerson(@PathVariable Long id, @RequestBody PersonDTO dto) {
        return personService.updatePerson(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        personService.deletePerson(id);
    }
}