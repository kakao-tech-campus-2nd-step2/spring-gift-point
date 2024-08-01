package gift.web.controller;

import gift.service.category.CategoryService;
import gift.web.dto.category.CategoryRequestDto;
import gift.web.dto.category.CategoryResponseDto;
import gift.web.dto.MemberDto;
import gift.web.jwt.AuthUser;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@AuthUser MemberDto memberDto, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@AuthUser MemberDto memberDto, @PathVariable Long id, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("DeleteSuccess", HttpStatus.OK);
    }
}