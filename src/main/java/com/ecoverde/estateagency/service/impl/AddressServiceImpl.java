package com.ecoverde.estateagency.service.impl;

import com.ecoverde.estateagency.model.entity.Address;
import com.ecoverde.estateagency.model.entity.Town;
import com.ecoverde.estateagency.model.service.AddressServiceModel;
import com.ecoverde.estateagency.model.service.TownServiceModel;
import com.ecoverde.estateagency.repositories.AddressRepository;
import com.ecoverde.estateagency.service.AddressService;
import com.ecoverde.estateagency.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, TownService townService, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public AddressServiceModel findByArea(String area) {
      return this.addressRepository.findByArea(area)
                .map(address -> this.modelMapper.map(address,AddressServiceModel.class))
                .orElse(null);

    }

    @Override
    public AddressServiceModel findByFullAddress(String fullAddress) {
        return this.addressRepository.findByFullAddress(fullAddress)
                .map(address -> this.modelMapper.map(address,AddressServiceModel.class))
                .orElse(null);
    }

    @Override
    public AddressServiceModel addAddress(AddressServiceModel addressServiceModel) {
        Address address = this.modelMapper.map(addressServiceModel,Address.class);
        if (this.addressRepository.findByFullAddress(addressServiceModel.getFullAddress()).isEmpty()){
          this.addressRepository.saveAndFlush(address);
        }
        return this.modelMapper.map(address,AddressServiceModel.class);
    }
}
