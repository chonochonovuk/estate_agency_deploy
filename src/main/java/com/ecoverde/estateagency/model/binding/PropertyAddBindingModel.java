package com.ecoverde.estateagency.model.binding;

import com.ecoverde.estateagency.model.validator.FieldMatch;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;



public class PropertyAddBindingModel {

    private String username;
    private String propertyName;
    private String propertyType;
    private String town;
    private String area;
    private String fullAddress;
    private Integer size;
    private Integer rooms;
    private Integer bathrooms;
    private Integer year;
    private LocalDate date;
    private BigDecimal price;
    private MultipartFile imageUrl;
    private String description;


    public PropertyAddBindingModel() {
    }

    @NotEmpty(message = "Fill the field, please")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Length(min = 6, message = "Minimum 6 characters length")
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }


    @Length(min = 5, message = "Minimum 5 characters length")
    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    @Length(min = 4, message = "Minimum 4 characters length")
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Length(min = 5, message = "Minimum 5 characters length")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Length(min = 10, message = "Minimum 10 characters length")
    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Min(value = 1,message = "Min size 1!")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Min(value = 1,message = "Min size 1!")
    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    @Min(value = 1,message = "Min size 1!")
    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }
    @Min(value = 1990,message = "Year must be after 1990!")
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


    @DecimalMin(value = "0",message = "Price must be positive number!")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(MultipartFile imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Length(min = 20,message = "Description length must be more than 20 characters!")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
