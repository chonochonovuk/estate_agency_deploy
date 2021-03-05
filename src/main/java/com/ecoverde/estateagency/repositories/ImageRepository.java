package com.ecoverde.estateagency.repositories;

import com.ecoverde.estateagency.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository  extends JpaRepository<Image,String> {
    Optional<Image> findImageByUrl(String url);
}
