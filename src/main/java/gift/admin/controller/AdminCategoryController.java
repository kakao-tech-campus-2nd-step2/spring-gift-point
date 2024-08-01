package gift.admin.controller;

import gift.admin.dto.LeafCategoryDTO;
import gift.category.dto.CategoryRequest;
import gift.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/categories")
@Controller
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("category", new LeafCategoryDTO());
        return "productIndex";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("category", new LeafCategoryDTO());
        return "admin/createCategory";
    }

    @PostMapping("/new")
    public String createPageSubmit(
        @ModelAttribute("leafCategoryDTO") @Valid LeafCategoryDTO leafCategoryDTO) {

        CategoryRequest categoryRequest = toRestRequest(leafCategoryDTO);

        categoryService.create(categoryRequest);
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}")
    public String updatePage(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.read(id));
        return "admin/updateCategory";
    }

    private CategoryRequest toRestRequest(LeafCategoryDTO leafCategoryDTO) {
        return new CategoryRequest(leafCategoryDTO.getName(), leafCategoryDTO.getColor(),
            leafCategoryDTO.getDescription(), leafCategoryDTO.getImageUrl());
    }
}
