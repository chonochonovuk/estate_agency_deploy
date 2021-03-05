package com.ecoverde.estateagency.repositories;

import com.ecoverde.estateagency.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,String> {
    Optional<Address> findByArea(String area);
    Optional<Address> findByFullAddress(String fullAddress);
}
