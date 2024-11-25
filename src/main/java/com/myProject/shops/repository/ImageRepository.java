package com.myProject.shops.repository;

import com.myProject.shops.dto.ImageDto;
import com.myProject.shops.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByProductId(Long id);
}