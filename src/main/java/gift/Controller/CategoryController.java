package gift.Controller;

import gift.DTO.CategoryResponseDTO;
import gift.Service.CategoryService;
import gift.util.CustomPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
@Tag(name = "상품 카테고리관련 서비스", description = " ")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "모든 카테고리 목록을 조회한다", description = " ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated list of products")
    })
    @GetMapping
    public CustomPageResponse<CategoryResponseDTO> getAllCategories() {
        List<CategoryResponseDTO> resultData = categoryService.getAllCategories().stream()
                .map(category -> new CategoryResponseDTO(
                        category.getLabel(),
                        category.getImageURL(),
                        category.getTitle(),
                        category.getDescription(),
                        category.getBackgroundColor(),
                        category.getId()
                ))
                .collect(Collectors.toList());

        return new CustomPageResponse<>(
                resultData,
                0,
                1,
                false,
                resultData.size()
        );
    }


//    @Operation(summary = "카테고리를 생성한다.", description = "카테고리명은 중복될 수 없습니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully created category"),
//            @ApiResponse(responseCode = "400", description = "Invalid input data")
//    })
//    @PostMapping
//    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
//        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
//        return ResponseEntity.ok(createdCategory);
//    }
//
//    @Operation(summary = "단일 카테고리를 조회한다.", description = "카테고리에 포함된 상품id, 카테고리id, 카테고리명, 생성일, 수정일을 반환합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved category"),
//            @ApiResponse(responseCode = "404", description = "Category not found")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
//        CategoryDTO categoryDTO = categoryService.findById(id);
//        return ResponseEntity.ok(categoryDTO);
//    }
//
//    @Operation(summary = "카테고리를 수정한다.", description = "카테고리명은 중복될 수 없습니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully updated category"),
//            @ApiResponse(responseCode = "404", description = "Category not found"),
//            @ApiResponse(responseCode = "400", description = "Invalid input data")
//    })
//    @PutMapping("/{id}")
//    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
//        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
//        return ResponseEntity.ok(updatedCategory);
//    }
}
