package com.ecoverde.estateagency.model.service;


public class AddressServiceModel extends BaseServiceModel {

    private String area;
    private String fullAddress;

    public AddressServiceModel() {
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
