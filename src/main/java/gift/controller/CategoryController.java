package gift.controller;


import gift.dto.category.ResponseCategoryDTO;
import gift.dto.category.SaveCategoryDTO;
import gift.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public List<ResponseCategoryDTO> getCategory() {
        return categoryService.getCategory();
    }

    @PostMapping("/api/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCategoryDTO saveCategory(@RequestBody SaveCategoryDTO saveCategoryDTO) {
        return categoryService.saveCategory(saveCategoryDTO);
    }

    @PutMapping("/api/categories/{id}")
    public ResponseCategoryDTO updateCategory(@PathVariable("id") int id, @RequestBody SaveCategoryDTO saveCategoryDTO) {
        return categoryService.updateCategory(id, saveCategoryDTO);
    }

    @DeleteMapping("/api/categories/{id}")
    public ResponseCategoryDTO deleteCategory(@PathVariable("id") int id) {
        return categoryService.deleteCategory(id);
    }

}
