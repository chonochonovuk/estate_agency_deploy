package com.ecoverde.estateagency.service.impl;

import com.ecoverde.estateagency.model.entity.Town;
import com.ecoverde.estateagency.model.service.TownServiceModel;
import com.ecoverde.estateagency.repositories.TownRepository;
import com.ecoverde.estateagency.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TownServiceModel findByName(String name) {
        return this.townRepository.findByName(name)
                .map(town -> this.modelMapper.map(town,TownServiceModel.class))
                .orElse(null);
    }

    @Override
    public TownServiceModel addTown(TownServiceModel townServiceModel) {
        Town town = this.modelMapper.map(townServiceModel,Town.class);
        if (this.townRepository.findByName(townServiceModel.getName()).isEmpty()){
           this.townRepository.saveAndFlush(town);
        }
        return this.modelMapper.map(town,TownServiceModel.class);
    }
}
