package gift.controller;

import gift.dto.CategoryDTO;
import gift.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 카테고리 관련 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 모든 카테고리 조회
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
     * 새로운 카테고리를 생성
     *
     * @param categoryDTO 생성할 카테고리 데이터
     * @return 생성된 카테고리 DTO
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
    }

    /**
     * 기존 카테고리 수정
     *
     * @param id          카테고리 ID
     * @param categoryDTO 수정할 카테고리 데이터
     * @return 수정된 카테고리 DTO
     */
    @PutMapping("/{category_id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("category_id") Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(categoryDTO);
    }

    /**
     * 카테고리 삭제
     *
     * @param id 카테고리 ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{category_id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("category_id") Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}