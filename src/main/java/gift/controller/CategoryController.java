package gift.controller;

import gift.auth.LoginUser;
import gift.domain.User;
import gift.dto.common.apiResponse.ApiResponseBody.SuccessBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import gift.dto.requestdto.CategoryRequestDTO;
import gift.dto.responsedto.CategoryResponseDTO;
import gift.service.AuthService;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api")
@Tag(name = "카테고리 api", description = "카테고리 api입니다")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthService authService;

    public CategoryController(CategoryService categoryService, AuthService authService) {
        this.categoryService = categoryService;
        this.authService = authService;
    }

    @PostMapping("/category")
    @Operation(summary = "카테고리 등록 api", description = "카테고리 등록 api입니다")
    @ApiResponse(responseCode = "200", description = "카테고리 등록 성공")
    public ResponseEntity<SuccessBody<Long>> addCategory(
        @Valid @RequestBody CategoryRequestDTO categoryRequestDTO,
        @LoginUser User user) {
        authService.authorizeAdminUser(user);
        Long categoryId = categoryService.addCategory(categoryRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.CREATED, "카테고리가 생성되었습니다.", categoryId);
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "카테고리 단일 조회 api", description = "카테고리 단일 조회 api입니다")
    @ApiResponse(responseCode = "200", description = "카테고리 단일 성공")
    public ResponseEntity<SuccessBody<CategoryResponseDTO>> getOneCategory(
        @PathVariable("id") Long categoryId) {
        CategoryResponseDTO categoryResponseDTO = categoryService.getOneCategory(categoryId);
        return ApiResponseGenerator.success(HttpStatus.OK, "id : " + categoryId + " 카테고리를 조회했습니다.",
            categoryResponseDTO);
    }

    @GetMapping("/categories")
    @Operation(summary = "카테고리 전체 조회 api", description = "카테고리 전체 조회 api입니다")
    @ApiResponse(responseCode = "200", description = "카테고리 전체 조회 성공")
    public ResponseEntity<SuccessBody<List<CategoryResponseDTO>>> getAllCategory() {
        List<CategoryResponseDTO> categoryResponseDTOList = categoryService.getAllCategories();

        return ApiResponseGenerator.success(HttpStatus.OK, "모든 카테고리를 조회했습니다.",
            categoryResponseDTOList);
    }

    @PutMapping("/category/{id}")
    @Operation(summary = "카테고리 수정 api", description = "카테고리 수정 api입니다")
    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
    public ResponseEntity<SuccessBody<Long>> updateCategory(
        @PathVariable("id") Long categoryId,
        @Valid @RequestBody CategoryRequestDTO categoryRequestDTO,
        @LoginUser User user) {
        authService.authorizeAdminUser(user);
        Long updatedCategoryId = categoryService.updateCategory(categoryId, categoryRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, "카테고리가 수정되었습니다.", updatedCategoryId);
    }

    @DeleteMapping("/category/{id}")
    @Operation(summary = "카테고리 삭제 api", description = "카테고리 삭제ㄴ api입니다")
    @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공")
    public ResponseEntity<SuccessBody<Long>> deleteCategory(
        @PathVariable("id") Long categoryId,
        @LoginUser User user){
        authService.authorizeAdminUser(user);
        Long deletedCategoryId = categoryService.deleteCategory(categoryId);
        return ApiResponseGenerator.success(HttpStatus.OK, "카테고리가 삭제되었습니다.", deletedCategoryId);
    }
}
