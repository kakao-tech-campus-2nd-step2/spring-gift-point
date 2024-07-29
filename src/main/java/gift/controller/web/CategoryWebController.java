package gift.controller.web;

import gift.dto.CategoryDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/categories")
@Tag(name = "Category Web API", description = "웹 카테고리 관련 API")
public class CategoryWebController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryWebController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "웹용 카테고리 조회", description = "웹에서 사용할 모든 카테고리를 조회합니다.")
    public String getCategoriesWeb(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category";
    }

    @GetMapping("/{id}")
    @Operation(summary = "카테고리 ID로 조회", description = "지정된 카테고리 ID에 해당하는 카테고리를 조회합니다.")
    public String getCategoryById(@PathVariable("id") int id, Model model) {
        CategoryDTO category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "categoryDetail";
    }
}
