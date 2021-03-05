package com.ecoverde.estateagency.service.impl;

import com.ecoverde.estateagency.model.entity.PropertyType;
import com.ecoverde.estateagency.model.service.PropertyTypeServiceModel;
import com.ecoverde.estateagency.repositories.PropertyTypeRepository;
import com.ecoverde.estateagency.service.PropertyTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyTypeServiceImpl implements PropertyTypeService {

    private final PropertyTypeRepository propertyTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PropertyTypeServiceImpl(PropertyTypeRepository propertyTypeRepository, ModelMapper modelMapper) {
        this.propertyTypeRepository = propertyTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PropertyTypeServiceModel> findAllTypes() {
        return this.propertyTypeRepository.findAll().stream()
                .map(propertyType -> this.modelMapper.map(propertyType,PropertyTypeServiceModel.class))
                .collect(Collectors.toList());

    }

    @Override
    public PropertyTypeServiceModel findByTypename(String typename) {
        return this.propertyTypeRepository.findByTypeName(typename)
                .map(propertyType -> this.modelMapper.map(propertyType,PropertyTypeServiceModel.class))
                .orElse(null);
    }

    @Override
    public void addPropertyType(PropertyTypeServiceModel propertyTypeServiceModel) {
        PropertyType propertyType = this.modelMapper.map(propertyTypeServiceModel, PropertyType.class);
        if (this.propertyTypeRepository.findByTypeName(propertyType.getTypeName()).isEmpty()){
            this.propertyTypeRepository.saveAndFlush(propertyType);
        }
    }
}
