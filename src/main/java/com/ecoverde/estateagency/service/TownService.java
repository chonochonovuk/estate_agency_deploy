package com.ecoverde.estateagency.service;

import com.ecoverde.estateagency.model.service.TownServiceModel;

public interface TownService {
    TownServiceModel findByName(String name);

    TownServiceModel addTown(TownServiceModel townServiceModel);
}
