package com.myProject.shops.service.categoryService;

import com.myProject.shops.exceptions.AlreadyExistsException;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Category;
import com.myProject.shops.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter((c) -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistsException(category.getName() + " is already exists"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);

    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category not found!!"));
    }

    @Override
    public List<Category> getCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository
                .findById(id)
                .ifPresentOrElse(categoryRepository::delete,
                        () -> {
                            new ResourceNotFoundException("category not found!!");
                        });


    }

    @Override
    public Category updateCategory(Long id, Category category) {

        return Optional.ofNullable(getCategoryById(id))
                .map((olderCategory) ->
                {
                    olderCategory.setName(category.getName());
                    return categoryRepository.save(olderCategory);
                })
                .orElseThrow(() -> new ResourceNotFoundException("category not found!!"));
    }
}
