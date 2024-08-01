package gift.controller;

import gift.entity.Category;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="카테고리 API")
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 목록 조회")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 카테고리 목록을 조회했습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class),
                    examples = @ExampleObject(
                            value = "[{\"id\": 1, \"name\": \"Electronics\", \"color\": \"#0000FF\", \"imgUrl\": \"http://example.com/image1.jpg\", \"description\": \"Various electronic products\"}, {\"id\": 2, \"name\": \"Books\", \"color\": \"#FF0000\", \"imgUrl\": \"http://example.com/image2.jpg\", \"description\": \"All kinds of books\"}]"
                    )
            )
    )
    @GetMapping
    public List<Category> getCategories() {
        return categoryService.findAll();
    }
}