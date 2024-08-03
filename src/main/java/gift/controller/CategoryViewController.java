package gift.controller;

import gift.service.CategoryService;
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
