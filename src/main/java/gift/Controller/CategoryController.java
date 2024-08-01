package gift.Controller;

import gift.DTO.CategoryDTO;
import gift.Model.Category;
import gift.Service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "Category 관련 API")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Operation(
        summary = "모든 카테고리",
        description = "등록된 모든 카테고리 가져오기"
    )
    @ApiResponse(
        responseCode = "200",
        description = "모든 카테고리 가져오기 성공"
    )
    @GetMapping("/api/categories")
    public ResponseEntity<List<Category>> getCategory(){
        return ResponseEntity.ok().body(categoryService.getAllCategory());
    }

    @Operation(
        summary = "특정 카테고리 가져오기",
        description = "해당되는 카테고리 가져오기"
    )
    @ApiResponse(
        responseCode = "200",
        description = "해당 카테고리 가져오기 성공"
    )
    @Parameter(name = "categoryId", description = "해당하는 카테고리 ID")
    @GetMapping("/api/categories/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable(value = "categoryId") Long categoryId){
        return ResponseEntity.ok().body(categoryService.getCategoryById(categoryId));
    }

    @Operation(
        summary = "카테고리 추가하기",
        description = "새로운 카테고리를 객체를 전달해 추가"
    )
    @ApiResponse(
        responseCode = "200",
        description = "카테고리 추가 성공"
    )
    @Parameter(name = "categoryDTO", description = "추가할 새로운 카테고리")
    @PostMapping("/api/categories")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok().body(categoryService.addCategory(categoryDTO));
    }

    @Operation(
        summary = "카테고리 수정",
        description = "전달된 카테고리로 수정"
    )
    @ApiResponse(
        responseCode = "200",
        description = "카테고리 수정 성공"
    )
    @Parameter(name = "categoryDTO", description = "해당하는 카테고리 객체")
    @PutMapping("/api/categories")
    public ResponseEntity<Category> updateCategory(@RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok().body(categoryService.updateCategory(categoryDTO));
    }

    @Operation(
        summary = "카테고리 삭제",
        description = "특정 카테고리 삭제"
    )
    @ApiResponse(
        responseCode = "200",
        description = "카테고리 삭제 성공"
    )
    @Parameter(name = "categoryId", description = "해당하는 카테고리 ID")
    @DeleteMapping("/api/categories/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable(value = "categoryId") Long categoryId){
        return ResponseEntity.ok().body(categoryService.deleteCategory(categoryId));
    }
}
