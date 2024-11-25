package com.myProject.shops.controller;

import com.myProject.shops.dto.ProductDto;
import com.myProject.shops.exceptions.AlreadyExistsException;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Product;
import com.myProject.shops.response.ApiResponse;
import com.myProject.shops.service.productService.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/allProduct")
    public ResponseEntity<ApiResponse> getProducts(){
        List<Product> products = productService.getProduct();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("products found",convertedProducts));
    }
    @GetMapping("/{id}/getProductById")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try {
            Product product = productService.getProductById(id);
            ProductDto productDto = productService.entityToDto(product);
            return ResponseEntity.ok(new ApiResponse("product found",productDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/{name}/getProductByName")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
        try {
            List<Product> products = productService.getProductByName(name);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("product not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("product found",convertedProducts));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/getProductByNameAndBrand")
    public ResponseEntity<ApiResponse> getProductByNameAndBrand(@RequestParam String name,@RequestParam String brand){
        try {
            List<Product> products = productService.getProductByNameAndBrand(name,brand);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("product not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("product found",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addProduct")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto dto){
        try {
            ProductDto product = productService.addProduct(dto);
            return ResponseEntity.ok(new ApiResponse("product added successfully",product));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}/deleteProduct")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new ApiResponse("product deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{brandName}/getProductByBrand")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brandName ) {
        try {
            List<Product> products = productService.getProductByBrand(brandName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("product not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("product found",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{category}/getProductByCategory")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category){
        try {
            List<Product> products = productService.getProductByCategory(category);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("product not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("products found ", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getProductByCategoryAndBrand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category,@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductByCategoryAndBrand(category, brand);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("product not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("product found", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/updateProduct")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDto dto,@PathVariable Long id) {
        try {
            Product product = productService.updateProduct(dto, id);
            ProductDto productDto = productService.entityToDto(product);
            return ResponseEntity.ok(new ApiResponse("product updated", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/countProductsByBrandAndName")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand,@RequestParam String name) {
        try {
            Long count = productService.countProductByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("products counted", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
