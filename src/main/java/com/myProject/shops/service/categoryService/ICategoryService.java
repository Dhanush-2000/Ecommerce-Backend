package com.myProject.shops.service.categoryService;

import com.myProject.shops.model.Category;

import java.util.List;

public interface ICategoryService {
    public Category addCategory(Category dto);
    public Category getCategoryByName(String name);
    public Category getCategoryById(Long id);
    public List<Category> getCategory();
    public void deleteCategoryById(Long id);
    public Category updateCategory(Long id,Category category);
}
