package gift.controller.admin;

import gift.model.dto.CategoryDTO;
import gift.model.form.CategoryForm;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminCategoriesController {

    private final CategoryService categoryService;

    public AdminCategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String getCategoryListPage(Model model) {
        List<CategoryDTO> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "categories";
    }

    @GetMapping("/categories/add")
    public String getCategoryForm(Model model, CategoryForm categoryForm) {
        model.addAttribute("category", categoryForm);
        return "category-add";
    }

    @PostMapping("/categories/add")
    public String addCategory(@Valid @ModelAttribute CategoryForm categoryForm) {
        CategoryDTO categoryDTO = new CategoryDTO(categoryForm.getName(), categoryForm.getImgUrl(),
            categoryForm.getDescription());
        categoryService.insertCategory(categoryDTO);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/update/{categoryId}")
    public String getCategoryUpdateForm(Model model, @PathVariable("categoryId") Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        CategoryForm categoryForm = new CategoryForm(categoryDTO.getCategoryId(), categoryDTO.getName(),
            categoryDTO.getImageUrl(), categoryDTO.getDescription());
        model.addAttribute("category", categoryForm);
        return "category-update";
    }

    @PutMapping("/categories/update/{categoryId}")
    public String udpateCategoryId(@Valid @ModelAttribute CategoryForm categoryForm) {
        System.out.println("카테고리 id= " + categoryForm.getId());
        CategoryDTO categoryDTO = new CategoryDTO(categoryForm.getId(), categoryForm.getName(),
            categoryForm.getImgUrl(), categoryForm.getDescription());
        categoryService.updateCategory(categoryDTO);
        return "redirect:/admin/categories";
    }

}
