package gift.controller;

import gift.dto.CategoryDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
@Tag(name = "Category Page Controller")
public class CategoryPageController {

    private final CategoryService categoryService;

    public CategoryPageController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 페이지 뷰")
    @GetMapping
    public String viewCategoryPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category";
    }

    @Operation(summary = "새 카테고리 생성 폼")
    @GetMapping("/new")
    public String createCategoryForm(Model model) {
        CategoryDTO categoryDTO = new CategoryDTO();
        model.addAttribute("category", categoryDTO);
        return "addCategory";
    }

    @Operation(summary = "새 카테고리 생성")
    @PostMapping("/new")
    public String createCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addCategory";
        }
        categoryService.create(categoryDTO);
        return "redirect:/categories";
    }

    @Operation(summary = "카테고리 업데이트 폼")
    @GetMapping("/update/{id}")
    public String updateCategoryForm(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.getById(id);
        model.addAttribute("category", categoryDTO);
        return "editCategory";
    }

    @Operation(summary = "카테고리 업데이트")
    @PutMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute("category") CategoryDTO categoryDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editCategory";
        }
        categoryService.update(id, categoryDTO);
        return "redirect:/categories";
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }
}
