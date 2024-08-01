package gift.controller;

import gift.dto.CategoryDto;
import gift.service.CategoryService;
import gift.utils.PageNumberListGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/categories")
public class CategoryViewController {

    private final CategoryService categoryService;

    public CategoryViewController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllCategoriesView(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<CategoryDto> categories = categoryService.getCategoryPage(pageable);
        model.addAttribute("pageNumbers",
            PageNumberListGenerator.generatePageNumberList(categories));
        model.addAttribute("categories", categories);

        return "category";
    }

    @GetMapping("/addForm")
    public String getCategoryAddForm(Model model) {
        return "categoryAddForm";
    }

    @GetMapping("/{id}")
    public String getCategoryEditForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("category", categoryService.getCategory(id));
        return "categoryEditForm";
    }
}
