package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByPlaceName(String placeName);
}