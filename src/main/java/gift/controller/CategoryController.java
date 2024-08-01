package gift.controller;

import gift.dto.CategoryRequestDTO;
import gift.dto.CategoryResponseDTO;
import gift.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 모든 카테고리 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // 특정 ID의 카테고리 조회
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // 새로운 카테고리 생성
    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        categoryService.createCategory(categoryRequestDTO);
        return ResponseEntity.status(201).build(); // 명세에 따라 Body 없음
    }

    // 기존 카테고리 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        categoryService.updateCategory(id, categoryRequestDTO);
        return ResponseEntity.ok("카테고리 정보를 수정했습니다."); // 명세에 따라 수정 완료 메시지 반환
    }

    // 특정 ID의 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
