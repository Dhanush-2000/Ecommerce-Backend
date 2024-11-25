package com.myProject.shops.service.productService;

import com.myProject.shops.dto.ProductDto;
import com.myProject.shops.model.Product;

import java.util.List;

public interface IProductService {

    ProductDto addProduct(ProductDto dto);
    void deleteProduct(Long id);
    Product updateProduct(ProductDto dto ,Long id);
    Product getProductById(Long id);
    List<Product> getProduct();
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategory(String category);
    List<Product> getProductByCategoryAndBrand(String category,String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByNameAndBrand(String name, String brand);
    Long countProductByBrandAndName(String brand,String name);

    ProductDto entityToDto(Product product);

    List<ProductDto> getConvertedProducts(List<Product> products);
}
