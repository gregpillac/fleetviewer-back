package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * GET /api/persons/byPlace{placeIdbyPlace{placeId} : Récupère une liste de personnes l'identifiant de leur site de rattachement.
     * @param placeId L'identifiant du site des personnes à récupérer.
     * @return ResponseEntity avec le statut 200 OK et le véhicule trouvé,
     * ou 404 Not Found si l'ID n'existe pas (géré par un exception handler).
     */
    @GetMapping("/byPlace{placeId}")
    public ResponseEntity<List<PersonDTO>> getPersonsByPlaceId (
            @PathVariable Long placeId) {
        List<PersonDTO> persons = personService.getVehiclesByPlaceId(placeId);
        return ResponseEntity.ok(persons);
    }
}