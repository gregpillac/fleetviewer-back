package com.eni.fleetviewer.back.repository;

import com.eni.fleetviewer.back.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}