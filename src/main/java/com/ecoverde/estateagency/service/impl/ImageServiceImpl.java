package com.ecoverde.estateagency.service.impl;

import com.ecoverde.estateagency.model.entity.Image;
import com.ecoverde.estateagency.repositories.ImageRepository;
import com.ecoverde.estateagency.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    @Override
    public Image findByUrl(String url) {
        return this.imageRepository.findImageByUrl(url).orElse(null);
    }

    @Override
    public void addImage(Image image) {
       if (this.imageRepository.findImageByUrl(image.getUrl()).isEmpty()){
           this.imageRepository.saveAndFlush(image);
       }
    }

}
