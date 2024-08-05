package gift.controller;

import gift.auth.CheckRole;
import gift.exception.InputException;
import gift.request.CategoryAddRequest;
import gift.request.CategoryUpdateRequest;
import gift.response.category.CategoryListResponse;
import gift.response.category.CategoryResponse;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryApiController {

    private final CategoryService categoryService;

    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @CheckRole("ROLE_USER")
    @GetMapping("/api/categories")
    public ResponseEntity<CategoryListResponse> getAllCategories() {
        List<CategoryResponse> dto = categoryService.getAllCategories();
        return new ResponseEntity<>(new CategoryListResponse(dto), HttpStatus.OK);
    }

    @CheckRole("ROLE_USER")
    @GetMapping("/api/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable("categoryId") Long id) {
        return new ResponseEntity<>(categoryService.getCategory(id), HttpStatus.OK);
    }

    @CheckRole("ROLE_USER")
    @PostMapping("/api/categories")
    public ResponseEntity<Void> addCategory(@RequestBody @Valid CategoryAddRequest dto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        categoryService.addCategory(dto.name(), dto.color(), dto.imageUrl(), dto.description());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @CheckRole("ROLE_USER")
    @PutMapping("/api/categories/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable("categoryId") Long id, @RequestBody @Valid CategoryUpdateRequest dto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        categoryService.updateCategory(id, dto.name(), dto.color(), dto.imageUrl(), dto.description());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CheckRole("ROLE_USER")
    @DeleteMapping("/api/categories")
    public ResponseEntity<Void> deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
