package gift.controller.thymeleaf;

import gift.controller.dto.request.CategoryRequest;
import gift.controller.dto.response.CategoryResponse;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String getCategories(Model model,
            @PageableDefault(size = 10) Pageable pageable) {
        model.addAttribute("categories", categoryService.findAllPaging(pageable));
        return "category/categories";
    }

    @GetMapping("/category/new")
    public String newCategory() {
        return "category/newCategory";
    }

    @GetMapping("/category/{id}")
    public String updateCategory(@PathVariable("id") @NotNull @Min(1) Long id, Model model) {
        CategoryResponse.Info category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category/editCategory";
    }

    @PostMapping("/category")
    public String createCategory(@Valid @ModelAttribute CategoryRequest.CreateCategory request) {
        categoryService.save(request);
        return "redirect:/admin/categories";
    }

    @PutMapping("/category")
    public String updateCategory(@Valid @ModelAttribute CategoryRequest.UpdateCategory request) {
        categoryService.updateById(request);
        return "redirect:/admin/categories";
    }

    @DeleteMapping("/category/{id}")
    public String delete(@PathVariable("id") @NotNull @Min(1) Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }
}
