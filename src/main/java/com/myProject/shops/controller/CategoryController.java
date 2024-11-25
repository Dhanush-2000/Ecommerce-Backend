package com.myProject.shops.controller;

import com.myProject.shops.exceptions.AlreadyExistsException;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Category;
import com.myProject.shops.response.ApiResponse;
import com.myProject.shops.service.categoryService.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/category/getAll")
    public ResponseEntity<ApiResponse> getCategories(){
        try {
            List<Category> category = categoryService.getCategory();
            return ResponseEntity.ok(new ApiResponse("added successfully",category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }


    }
    @GetMapping("/category/{id}/categoryById")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("category found",category));
        } catch (ResourceNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("category not found",null));
        }
    }

    @PostMapping ("/category/addCategory")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            Category AddedCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("category added",AddedCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/category/{name}/categoryByName")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("category found",category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Category not found",new ResourceNotFoundException("category not fount with the name"+name)));
        }

    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id){
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("deleted successfully",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category,@PathVariable Long id){
        try {
            Category theCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(new ApiResponse("category updated",theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }

}
