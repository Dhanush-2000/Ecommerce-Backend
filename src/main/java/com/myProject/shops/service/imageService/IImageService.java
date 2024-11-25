package com.myProject.shops.service.imageService;

import com.myProject.shops.dto.ImageDto;
import com.myProject.shops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file,Long imageId);


}
