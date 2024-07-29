package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import gift.dto.CategoryDto;
import gift.dto.response.CategoryResponse;

@RestController
@Tag(name = "category", description = "카테고리 API")
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    
    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 조회 성공")
    })
    public ResponseEntity<CategoryResponse> getCategories(){
        CategoryResponse categoryResponse = categoryService.findAll();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "카테고리를 생성 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 카테고리")
    })
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDto categoryDto){
        categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
