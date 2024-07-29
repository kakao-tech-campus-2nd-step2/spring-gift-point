package gift.product.controller;

import gift.product.dto.CategoryDTO;
import gift.product.model.Category;
import gift.product.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String category(Model model, Pageable pageable) {
        model.addAttribute("categoryList", categoryService.findAllCategory(pageable));
        return "category";
    }

    @GetMapping("/register")
    public String registerCategoryForm(Model model) {
        System.out.println("[AdminCategoryController] registerCategoryForm()");
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "category-form";
    }

    @PostMapping
    public String registerCategory(@Valid @ModelAttribute CategoryDTO categoryDTO, BindingResult bindingResult, Model model) {
        System.out.println("[AdminCategoryController] registerCategory()");
        if(bindingResult.hasErrors()) {
            System.out.println("[AdminCategoryController] registerCategory(): validation error: " + bindingResult.getAllErrors() + ", categoryDTO: " + categoryDTO.getName());
            model.addAttribute("categoryDTO", categoryDTO);
            return "category-form";
        }
        categoryService.registerCategory(categoryDTO.convertToDomain());
        return "redirect:/admin/category";
    }

    @GetMapping("/{id}")
    public String updateCategoryForm(@PathVariable Long id, Model model) {
        System.out.println("[AdminCategoryController] updateCategoryForm()");
        model.addAttribute("categoryDTO", categoryService.findCategoryById(id));
        return "category-update-form";
    }

    @PutMapping("/{id}")
    public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute CategoryDTO categoryDTO, BindingResult bindingResult, Model model) {
        System.out.println("[AdminCategoryController] editCategoryForm()");
        if(bindingResult.hasErrors()) {
            model.addAttribute("categoryDTO", categoryDTO);
        }
        categoryService.updateCategory(new Category(id, categoryDTO.getName()));
        return "redirect:/admin/category";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id) {
        System.out.println("[AdminCategoryController] deleteCategory()");
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }
}
