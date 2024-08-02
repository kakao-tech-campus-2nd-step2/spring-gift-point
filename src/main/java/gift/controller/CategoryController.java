package gift.controller;

import gift.DTO.Category.CategoryRequest;
import gift.DTO.Category.CategoryResponse;
import gift.DTO.User.UserResponse;
import gift.security.AuthenticateMember;
import gift.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    /*
     * 카테고리 조회
     */
    @GetMapping("api/categories")
    public ResponseEntity<List<CategoryResponse>> readCategory(){
        List<CategoryResponse> categories = categoryService.findAll();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    /*
     * 카테고리 추가
     */
    @PostMapping("/api/categories")
    public ResponseEntity<Void> createCategory(
            @RequestBody CategoryRequest categoryRequest,
            @AuthenticateMember UserResponse user
    ){
        categoryService.save(categoryRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /*
     * 카테고리 수정
     */
    @PutMapping("/api/categories/{category_id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable("category_id") Long categoryId,
            @RequestBody CategoryRequest categoryRequest,
            @AuthenticateMember UserResponse user
    ){
        categoryService.update(categoryId, categoryRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    /*
     * 카테고리 삭제
     */
    @DeleteMapping("/api/categories/{category_id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("category_id") Long categoryId,
            @AuthenticateMember UserResponse user
    ){
        categoryService.delete(categoryId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
