package gift.controller;

import gift.classes.RequestState.CategoryRequestStateDTO;
import gift.classes.RequestState.RequestStateDTO;
import gift.classes.RequestState.RequestStatus;
import gift.dto.CategoryDto;
import gift.dto.RequestCategoryDto;
import gift.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/categories")
@Tag(name = "CategoryController", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //    Categories 조회
    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "모든 카테고리 조회 성공")
    })
    public ResponseEntity<CategoryRequestStateDTO> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();

        return ResponseEntity.ok().body(new CategoryRequestStateDTO(
            RequestStatus.success,
            null,
            categories
        ));
    }

    //    Category 추가
    @PostMapping
    @Operation(summary = "카테고리 추가", description = "카테고리를 추가할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 추가 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<RequestStateDTO> addCategory(
        @RequestBody RequestCategoryDto requestCategoryDto) {
        categoryService.addCategory(requestCategoryDto);
        return ResponseEntity.ok().body(new RequestStateDTO(
            RequestStatus.success,
            null
        ));
    }

    //    Category 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "카테고리 없음 - 해당 ID로 카테고리를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

}
