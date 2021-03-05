package com.ecoverde.estateagency.service;

import com.ecoverde.estateagency.model.entity.Address;
import com.ecoverde.estateagency.model.service.AddressServiceModel;

public interface AddressService {
    AddressServiceModel findByArea(String area);
    AddressServiceModel findByFullAddress(String fullAddress);
    AddressServiceModel addAddress(AddressServiceModel addressServiceModel);
}
