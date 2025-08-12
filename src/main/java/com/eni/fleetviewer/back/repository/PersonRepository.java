package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // JpaRepository fournit déjà : save(), findById(), findAll(), deleteById(), etc.
}