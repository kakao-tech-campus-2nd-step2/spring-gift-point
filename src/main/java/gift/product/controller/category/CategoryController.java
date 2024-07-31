package gift.product.controller.category;


import gift.product.dto.category.CategoryDto;
import gift.product.exception.ExceptionResponse;
import gift.product.model.Category;
import gift.product.service.CategoryService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "category", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    @GetMapping
    public ResponseEntity<List<Category>> getCategoryAll() {
        List<Category> categories = categoryService.getCategoryAll();
        return ResponseEntity.ok(categories);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable(name = "id") Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Void> insertCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.insertCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "카테고리 수정 성공"),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable(name = "id") Long id,
        @RequestBody CategoryDto categoryDto) {
        categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
