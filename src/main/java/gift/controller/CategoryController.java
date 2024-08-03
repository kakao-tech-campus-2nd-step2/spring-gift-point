package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.category.CategoriesResponseDTO;
import gift.dto.category.CategoryRequestDTO;
import gift.dto.category.CategoryResponseDTO;
import gift.model.User;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/categories")
@Tag(name = "Category API", description = "카테고리 API")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회",
            description = "모든 카테고리 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            })
    public ResponseEntity<CategoriesResponseDTO> getAllCategories() {
        CategoriesResponseDTO categoriesResponseDTO = categoryService.getAllCategories();

        return new ResponseEntity<>(categoriesResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "새 카테고리 추가",
            description = "주어진 세부 정보로 새로운 카테고리를 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "카테고리 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못 된 값 입력, 중복된 값"),
                    @ApiResponse(responseCode = "401", description = "인증 오류"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            })
    public ResponseEntity<CategoryResponseDTO> addCategory(@LoginUser User user,
                                                           @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.addCategory(categoryRequestDTO);

        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "기존 카테고리 수정",
            description = "기존 카테고리의 세부 정보를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못 된 값 입력"),
                    @ApiResponse(responseCode = "401", description = "인증 오류"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 categoryId"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            })
    public ResponseEntity<CategoryResponseDTO> modifyCategory(@LoginUser User user,
                                                              @PathVariable(name = "categoryId") Long categoryId,
                                                              @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.modifyCategory(categoryId, categoryRequestDTO);

        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
    }
}
