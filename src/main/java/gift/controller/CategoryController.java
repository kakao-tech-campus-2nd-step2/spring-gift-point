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
    public ResponseEntity<Page<CategoryResponse>> readCategory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort,
            @RequestParam(value = "field", defaultValue = "id") String field
    ){
        if(sort.equals("asc")){
            Page<CategoryResponse> categories = categoryService.findAllASC(page, size, field);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        Page<CategoryResponse> categories = categoryService.findAllDESC(page, size, field);
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
            @PathVariable Long category_id,
            @RequestBody CategoryRequest categoryRequest,
            @AuthenticateMember UserResponse user
    ){
        categoryService.update(category_id, categoryRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    /*
     * 카테고리 삭제
     */
    @DeleteMapping("/api/categories/{category_id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long category_id,
            @AuthenticateMember UserResponse user
    ){
        categoryService.delete(category_id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
