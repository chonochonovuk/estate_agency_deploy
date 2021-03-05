package com.ecoverde.estateagency.repositories;

import com.ecoverde.estateagency.model.entity.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyTypeRepository extends JpaRepository<PropertyType,String> {
    Optional<PropertyType> findByTypeName(String typeName);
}
