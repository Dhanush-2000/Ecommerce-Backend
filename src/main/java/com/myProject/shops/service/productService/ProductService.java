package com.myProject.shops.service.productService;

import com.myProject.shops.dto.ImageDto;
import com.myProject.shops.dto.ProductDto;
import com.myProject.shops.exceptions.AlreadyExistsException;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Category;
import com.myProject.shops.model.Image;
import com.myProject.shops.model.Product;
import com.myProject.shops.repository.CategoryRepository;
import com.myProject.shops.repository.ImageRepository;
import com.myProject.shops.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, CategoryRepository categoryRepository,
                          ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public ProductDto addProduct(ProductDto dto) {
        if (productExists(dto.getName(),dto.getBrand())){
            throw new AlreadyExistsException(dto.getName()+" "+dto.getBrand()+" already exists, you may update the product instead");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(dto.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(dto.getCategory().getName());
                    return categoryRepository.save(newCategory);

                });
        dto.setCategory(category);
        Product savedProduct = productRepository.save(dtoToEntity(dto));
        return entityToDto(savedProduct);
    }

    private boolean productExists(String name,String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse((productRepository::delete),
                        ()->{throw new ResourceNotFoundException("product not found");});


    }

    @Override
    public Product updateProduct(ProductDto dto, Long id) {
        return productRepository.findById(id)
                .map(existingProduct->updateExistingProduct(existingProduct,dto))
                .map(productRepository::save)
                .orElseThrow(()->new ResourceNotFoundException("product not found!!"));

    }

    @Override
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("product not found!!"));
    }

    @Override
    public List<Product> getProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductByName(String name) {

        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByNameAndBrand(String name, String brand) {
        return productRepository.findByNameAndBrand(name,brand);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {

        return productRepository.countByBrandAndName(brand,name);
    }


    private Product dtoToEntity(ProductDto dto){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Product product = modelMapper.map(dto, Product.class);
        return product;
    }
    @Override
    public ProductDto entityToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> image = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageList = image.stream()
                .map(img -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImage(imageList);
        return productDto;
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::entityToDto).toList();
    }

    private Product updateExistingProduct(Product existingProduct,ProductDto dto){
        existingProduct.setId(dto.getId());
        existingProduct.setBrand(dto.getBrand());
        existingProduct.setName(dto.getName());
        existingProduct.setInventory(dto.getInventory());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setDescription(dto.getDescription());
        Category category = categoryRepository.findByName(dto.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }
}
