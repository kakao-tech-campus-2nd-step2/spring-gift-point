package gift.controller.rest;

import gift.entity.Category;
import gift.entity.CategoryDTO;
import gift.entity.MessageResponseDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category 컨트롤러", description = "Category API입니다.")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 카테고리 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Category.class)))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping()
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 생성 성공",
                    content = @Content(schema = @Schema(implementation = Category.class)))
    })
    @PostMapping()
    public ResponseEntity<Category> postCategory(@RequestBody CategoryDTO form) {
        Category result = categoryService.save(form);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 조회", description = "id로 카테고리를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        Category result = categoryService.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 편집", description = "id로 카테고리를 편집합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 편집 성공",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "대상 카테고리 조회 실패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category> putCategory(@PathVariable Long id, @RequestBody CategoryDTO form) {
        Category result = categoryService.update(id, form);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 삭제", description = "id로 카테고리를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity
                .ok()
                .body(new MessageResponseDTO("deleted successfully"));
    }
}
