package com.ecoverde.estateagency.service;


import com.ecoverde.estateagency.model.entity.Image;



public interface ImageService {
   Image findByUrl(String url);
   void addImage(Image image);

}
