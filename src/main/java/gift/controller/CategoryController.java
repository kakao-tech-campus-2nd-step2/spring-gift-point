package gift.controller;

import gift.dto.CategoryDTO;
import gift.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 모든 카테고리 조회.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 카테고리 목록
     */
    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> readCategory(@RequestParam int page, @RequestParam int size) {
        Page<CategoryDTO> categories = categoryService.findAll(page, size);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * 새로운 카테고리 생성.
     *
     * @param categoryDTO 카테고리 DTO
     * @return 응답 엔티티
     */
    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 기존 카테고리 수정.
     *
     * @param id 카테고리 ID
     * @param categoryDTO 카테고리 DTO
     * @return 응답 엔티티
     */
    @PutMapping("/{category_id}")
    public ResponseEntity<Void> updateCategory(@PathVariable("category_id") Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryService.update(id, categoryDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 카테고리 삭제.
     *
     * @param id 카테고리 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{category_id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("category_id") Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}