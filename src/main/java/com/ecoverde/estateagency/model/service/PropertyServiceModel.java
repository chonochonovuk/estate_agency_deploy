package com.ecoverde.estateagency.model.service;

import com.ecoverde.estateagency.model.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class PropertyServiceModel extends BaseServiceModel{

    private String propertyName;
    private PropertyTypeServiceModel propertyTypeServiceModel;
    private TownServiceModel townServiceModel;
    private AddressServiceModel addressServiceModel;
    private Integer size;
    private Integer rooms;
    private Integer bathrooms;
    private Integer year;
    private LocalDate date;
    private String description;
    private BigDecimal price;
    private UserServiceModel owner;
    private Image photos;

    public PropertyServiceModel() {
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public PropertyTypeServiceModel getPropertyTypeServiceModel() {
        return propertyTypeServiceModel;
    }

    public void setPropertyTypeServiceModel(PropertyTypeServiceModel propertyTypeServiceModel) {
        this.propertyTypeServiceModel = propertyTypeServiceModel;
    }

    public TownServiceModel getTownServiceModel() {
        return townServiceModel;
    }

    public void setTownServiceModel(TownServiceModel townServiceModel) {
        this.townServiceModel = townServiceModel;
    }

    public AddressServiceModel getAddressServiceModel() {
        return addressServiceModel;
    }

    public void setAddressServiceModel(AddressServiceModel addressServiceModel) {
        this.addressServiceModel = addressServiceModel;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UserServiceModel getOwner() {
        return owner;
    }

    public void setOwner(UserServiceModel owner) {
        this.owner = owner;
    }

    public Image getPhotos() {
        return photos;
    }

    public void setPhotos(Image photos) {
        this.photos = photos;
    }
}
