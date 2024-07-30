package gift.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.entity.Category;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "카테고리 관리", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@Operation(summary = "모든 카테고리 조회", description = "모든 카테고리 정보를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 리스트 반환 성공")
	@GetMapping
	public ResponseEntity<List<Category>> getAllCategories(){
		List<Category> categories = categoryService.getAllCategories();
		return ResponseEntity.status(HttpStatus.OK).body(categories);
	}
	
	@Operation(summary = "카테고리 추가", description = "새 카테고리를 추가합니다.")
    @ApiResponse(responseCode = "201", description = "카테고리 추가 성공")
	@PostMapping
	public ResponseEntity<Void> addCategory(@Valid @RequestBody Category category, BindingResult bindingResult){
		categoryService.createCategory(category, bindingResult);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
	@PutMapping("/{categoryId}")
	public ResponseEntity<Void> updateCategory(@PathVariable("categoryId") Long categoryId, @Valid Category category,
			BindingResult bindingResult){
		categoryService.updateCategory(categoryId, category, bindingResult);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
