package com.ecoverde.estateagency.repositories;

import com.ecoverde.estateagency.model.entity.Address;
import com.ecoverde.estateagency.model.entity.Property;
import com.ecoverde.estateagency.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface PropertyRepository extends JpaRepository<Property,String> {

    Optional<Property> findByPropertyName(String propertyName);

    @Query("SELECT p FROM Property p WHERE p.owner.username = :username")
    Set<Property> findAllByOwnerUsername(@Param("username") String username);

    @Query("SELECT p FROM Property p WHERE p.owner.enabled = false")
    Set<Property> findAllByOwnerNotEnabled();

    @Modifying
    @Query("DELETE FROM Property p WHERE p.propertyName = :propertyName")
    void deleteByPropertyName(@Param("propertyName") String propertyName);

    @Query("SELECT p FROM Property p WHERE p.description LIKE CONCAT('%',:keyword,'%') ")
    Set<Property> findAllByDescriptionContaining(@Param("keyword") String keyword);

    @Query("SELECT p FROM Property p WHERE p.propertyType.typeName = :propertyType ")
    Set<Property> findAllByPropertyType(@Param("propertyType") String propertyType);

    @Query("SELECT p FROM Property p WHERE p.town.name = :townOrAddress OR p.address.area = :townOrAddress OR p.address.fullAddress LIKE CONCAT('%',:townOrAddress,'%')")
    Set<Property> findAllByTownOrAddress(@Param("townOrAddress") String townOrAddress);

    @Query("SELECT p FROM Property p WHERE p.price <= :price ")
    Set<Property> findAllByPrice(@Param("price") BigDecimal price);

    @Query("SELECT p FROM Property p WHERE p.propertyType.typeName = :propertyType AND p.price <= :price")
    Set<Property> findAllByPropertyTypeAndPrice(@Param("propertyType") String propertyType,@Param("price") BigDecimal price);

    @Query("SELECT p FROM Property p WHERE p.description LIKE CONCAT('%',:keyword,'%') AND p.propertyType.typeName = :propertyType AND p.price <= :price")
    Set<Property> findAllByDescriptionContainingAndPropertyTypeAndPrice(@Param("keyword") String keyword,@Param("propertyType") String propertyType,@Param("price") BigDecimal price);

    @Query("SELECT p FROM Property p WHERE p.propertyType.typeName = :propertyType AND p.price <= :price AND p.town.name = :townOrAddress OR p.address.area = :townOrAddress")
    Set<Property> findAllByPropertyTypeAndPriceAndTownOrAddress(@Param("propertyType") String propertyType,@Param("price") BigDecimal price,@Param("townOrAddress") String townOrAddress);

    @Query("SELECT p FROM Property p WHERE p.description LIKE CONCAT('%',:keyword,'%') AND p.propertyType.typeName = :propertyType AND p.price <= :price AND p.town.name = :townOrAddress OR p.address.area = :townOrAddress")
    Set<Property> findAllByDescriptionContainingAndPropertyTypeAndPriceAndTownOrAddress(@Param("keyword") String keyword,@Param("propertyType") String propertyType,@Param("price") BigDecimal price,@Param("townOrAddress") String townOrAddress);

}
