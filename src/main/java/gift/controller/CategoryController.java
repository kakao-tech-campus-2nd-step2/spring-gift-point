package gift.controller;

import gift.dto.request.CategoryRequestDto;
import gift.dto.response.CategoryResponseDto;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories(){
        List<CategoryResponseDto> categories = categoryService.findAllCategories();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable("id") Long categoryId){
        CategoryResponseDto categoryResponseDto = categoryService.findOneCategoryById(categoryId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(categoryResponseDto);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto){
        CategoryResponseDto categoryResponseDto = categoryService.saveCategory(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(categoryResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> editCategory(@PathVariable("id") Long categoryId,
                                                            @RequestBody @Valid CategoryRequestDto categoryRequestDto){
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(categoryId, categoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(categoryResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable("id") Long categoryId){
        CategoryResponseDto categoryResponseDto = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(categoryResponseDto);
    }
}
