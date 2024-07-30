package gift.controller;

import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.entity.Category;
import gift.exception.CategoryException;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "카테고리 목록을 조회합니다.",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDto.class))))
        })
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponseDto> categoriesDto = categories.stream()
            .map(Category::toResponseDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(categoriesDto, HttpStatus.OK);
    }


    @Operation(summary = "카테고리 추가", description = "카테고리를 추가합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "카테고리를 추가합니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "이미 존재하는 카테고리입니다."
            )
        }
    )
    @PostMapping
    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        if (categoryService.existsByName(categoryRequestDto.getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Category category = categoryService.save(categoryRequestDto);
        return new ResponseEntity<>(category.toResponseDto(), HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "카테고리를 수정합니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 카테고리입니다."
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
        try {
            Category category = categoryService.update(categoryRequestDto, id);
            return new ResponseEntity<>(category.toResponseDto(), HttpStatus.OK);
        } catch (CategoryException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
