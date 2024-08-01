package gift.controller;

import gift.dto.ApiResponse;
import gift.model.Category;
import gift.model.HttpResult;
import gift.service.CategoryService;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse> getAllCategories() {
        var allCategories = categoryService.getAllCategories();
        var getAllCateogiresSucess = new ApiResponse(HttpResult.OK, allCategories.toString(),
            HttpStatus.OK);
        return new ResponseEntity<>(getAllCateogiresSucess, getAllCateogiresSucess.getHttpStatus());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse> getCategory(@PathVariable("id") Long id) {
        var foundCategory = categoryService.getCategoryById(id);
        var getAllCateogiresSucess = new ApiResponse(HttpResult.OK,
            Collections.singletonList(foundCategory).toString(),
            HttpStatus.OK);
        return new ResponseEntity<>(getAllCateogiresSucess, getAllCateogiresSucess.getHttpStatus());
    }


    @PostMapping("/add/category")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        var addCategorySucessResponse = new ApiResponse(HttpResult.OK, categoryService.addCategory(category).toString(),
            HttpStatus.OK);
        return new ResponseEntity<>(addCategorySucessResponse,
            addCategorySucessResponse.getHttpStatus());
    }

    @PutMapping("/update/category")
    public ResponseEntity<ApiResponse> updateCategoryName(@RequestParam("id") Long id,
        @RequestBody Category category) {
        var categoryDto = categoryService.updateCategory(id, category);
        var apiResponse = new ApiResponse(HttpResult.OK, categoryDto.toString(), HttpStatus.OK);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @DeleteMapping("/delete/category")
    public ResponseEntity<ApiResponse> deleteCategoryById(@RequestParam("id") Long id) {
        categoryService.deleteCategoryById(id);
        var deleteSucess = new ApiResponse(HttpResult.OK, "삭제 성공", HttpStatus.OK);
        return new ResponseEntity<>(deleteSucess, deleteSucess.getHttpStatus());
    }
}
