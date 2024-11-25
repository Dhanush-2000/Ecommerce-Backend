package com.myProject.shops.controller;

import com.myProject.shops.dto.ImageDto;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Image;
import com.myProject.shops.response.ApiResponse;
import com.myProject.shops.service.imageService.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> files, @RequestParam Long productId){
        try {
            List<ImageDto> imageDto = imageService.saveImage(files, productId);
            return ResponseEntity.ok(new ApiResponse("successfully saved",imageDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("upload failed",e.getMessage()));
        }


    }

    @GetMapping("/image/downloadUrl/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable Long imageId) throws SQLException {
        Image image =imageService.getImageById(imageId);

            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int)image.getImage().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+ image.getFileName()+"\"")
                    .body(resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,@RequestBody MultipartFile file){
        try {
            Image presentImage = imageService.getImageById(imageId);
            if(presentImage!=null){
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("updated successfully",null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("update failed",INTERNAL_SERVER_ERROR));


    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            if (image!=null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok().body(new ApiResponse("image deleted",null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("image not found",e.getMessage()));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed to delete",INTERNAL_SERVER_ERROR));
    }
}
