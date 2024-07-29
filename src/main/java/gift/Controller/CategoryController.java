package gift.Controller;


import gift.Model.Entity.Category;
import gift.DTO.RequestCategoryDTO;
import gift.DTO.ResponseCategoryDTO;
import gift.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Category", description ="Category API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Operation(summary="카테고리 추가", description = "카테고리를 추가합니다")
    @ApiResponse(responseCode = "201", description = "추가 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요.")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @PostMapping
    public ResponseEntity<String> addCategory(@RequestBody RequestCategoryDTO requestCategoryDTO){
        Category category = categoryService.addCategory(requestCategoryDTO);
        return ResponseEntity.created(URI.create("api/categories/"+ category.getId())).body("카테고리가 정상적으로 추가되었습니다");
    }


    @Operation(summary = "카테고리 목록 조회", description = "등록된 카테고리를 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 완료",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseCategoryDTO.class)))
    })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @GetMapping
    public ResponseEntity<List<ResponseCategoryDTO>> getCategories (){
        List<ResponseCategoryDTO> categoryList = categoryService.getCategories();
        return ResponseEntity.ok(categoryList);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다")
    @ApiResponse(responseCode = "200", description = "수정 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @PutMapping
    public ResponseEntity <String> editCategory(@RequestBody RequestCategoryDTO requestCategoryDTO){
        categoryService.editCategory(requestCategoryDTO);
        return ResponseEntity.ok("카테고리를 정상적으로 수정하였습니다");
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다")
    @ApiResponse(responseCode = "200", description = "삭제 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @DeleteMapping("/{category-id}")
    public ResponseEntity<String> deleteCategory(@PathVariable ("category-id") Long categoryId ){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리가 정상적으로 삭제되었습니다");
    }
}
