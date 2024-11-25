package com.myProject.shops.service.imageService;

import com.myProject.shops.dto.ImageDto;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Image;
import com.myProject.shops.model.Product;
import com.myProject.shops.repository.ImageRepository;
import com.myProject.shops.repository.ProductRepository;
import com.myProject.shops.service.productService.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;


    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("image not found!!"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete
                ,()->{throw new ResourceNotFoundException("image not found!!");});

    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> imageDto= new ArrayList<>();
        for(MultipartFile file:files){

                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
            try {
                image.setImage(new SerialBlob(file.getBytes()));
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            String buildDownloadUrl ="/api/v1/images/image/downloadUrl/";
            String downloadUrl = buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);
                image.setProduct(product);
            Image savedImage = imageRepository.save(image);
            savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
            imageRepository.save(savedImage);
            ImageDto dto = new ImageDto();
            dto.setId(savedImage.getId());
            dto.setFileName(savedImage.getFileName());
            dto.setDownloadUrl(savedImage.getDownloadUrl());
            imageDto.add(dto);
        }
        return imageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image oldImage = getImageById(imageId);
        try {
            oldImage.setFileName(file.getOriginalFilename());
            oldImage.setFileType(file.getContentType());
            oldImage.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(oldImage);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }


    }
}
