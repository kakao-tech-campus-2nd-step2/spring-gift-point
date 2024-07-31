package gift.controller;

import gift.constants.ResponseMsgConstants;
import gift.dto.betweenClient.category.CategoryDTO;
import gift.dto.betweenClient.ResponseDTO;
import gift.dto.swagger.GetCategoryResponse;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/categories")
public class CategoryController {

    CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(description = "서버가 클라이언트에게 카테고리들을 제공합니다.", tags = "Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 카테고리를 제공합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCategoryResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<Map<String, List<CategoryDTO>>> getCategories() {
        Map<String, List<CategoryDTO>> response = new HashMap<>();
        response.put("categories", categoryService.getCategories());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @Hidden
    public ResponseEntity<ResponseDTO> addCategory(@Validated @RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Hidden
    public ResponseEntity<ResponseDTO> updateCategory(@PathVariable Long id, @Validated @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Hidden
    public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.NO_CONTENT);
    }
}
