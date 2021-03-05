package com.ecoverde.estateagency.service;

import com.ecoverde.estateagency.model.service.PropertyTypeServiceModel;

import java.util.List;

public interface PropertyTypeService {
    List<PropertyTypeServiceModel> findAllTypes();

    PropertyTypeServiceModel findByTypename(String typename);

    void addPropertyType(PropertyTypeServiceModel propertyTypeServiceModel);
}
