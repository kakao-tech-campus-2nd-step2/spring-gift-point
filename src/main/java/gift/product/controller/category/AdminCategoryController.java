package gift.product.controller.category;

import gift.product.dto.category.CategoryDto;
import gift.product.model.Category;
import gift.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Hidden
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    public static final String REDIRECT_ADMIN_CATEGORIES = "redirect:/admin/categories";
    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String categories(Model model) {
        List<Category> categories = categoryService.getCategoryAll();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    @GetMapping("/insert")
    public String insertForm() {
        return "admin/insertCategoryForm";
    }

    @PostMapping("/insert")
    public String insertCategory(@Valid CategoryDto categoryDto) {
        categoryService.insertCategory(categoryDto);
        return REDIRECT_ADMIN_CATEGORIES;
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable(name = "id") Long categoryId, Model model) {
        Category category = categoryService.getCategory(categoryId);
        model.addAttribute("category", category);
        return "admin/updateCategoryForm";
    }

    @PutMapping("/update/{id}")
    public String updateCategory(@PathVariable(name = "id") Long categoryId,
        CategoryDto categoryDto) {
        categoryService.updateCategory(categoryId, categoryDto);
        return REDIRECT_ADMIN_CATEGORIES;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return REDIRECT_ADMIN_CATEGORIES;
    }
}
