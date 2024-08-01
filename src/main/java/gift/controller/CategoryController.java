package gift.controller;

import gift.domain.Category.Category;
import gift.domain.Category.CategoryRequest;
import gift.domain.Category.CategoryResponse;
import gift.service.CategoryService;
import io.jsonwebtoken.JwtException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<String> create(
            @RequestBody CategoryRequest categoryRequest
    ) throws BadRequestException {
        categoryService.create(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("successfully created");
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<String> update(
            @RequestBody CategoryRequest categoryRequest
    ) {
        categoryService.update(categoryRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("successfully created");
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategoryList(){
        return ResponseEntity.ok().body(categoryService.getCategotyList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable("id") Long id
    ){
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("successfully deleted");
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyExistsException(BadRequestException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("categoryNameError", "이미 존재하는 이메일입니다 다른 이메일을 사용해주세요");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleException(JwtException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("categoryError", "허용되지 않는 요청입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }
}
