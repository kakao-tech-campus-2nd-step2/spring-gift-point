package gift.adminController;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {

	private final CategoryService categoryService;

	public CategoryAdminController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public String showCategoriesPage(Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		return "categoryAdmin/admin-categories";
	}

	@GetMapping("/create")
	public String showCreateCategoryPage(Model model) {
		model.addAttribute("category", new CategoryRequest());
		return "categoryAdmin/category-create";
	}

	@PostMapping
	public String createCategory(@Valid @ModelAttribute("category") CategoryRequest categoryRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "categoryAdmin/category-create";
		}
		categoryService.createCategory(categoryRequest, bindingResult);
		return "redirect:/admin/categories";
	}

	@GetMapping("/{categoryId}")
	public String showEditCategoryPage(@PathVariable("categoryId") Long categoryId, Model model) {
		CategoryResponse category = categoryService.getCategoryById(categoryId).toDto();
		model.addAttribute("category", category);
		return "categoryAdmin/category-edit";
	}

	@PutMapping("/{categoryId}")
	public String updateCategory(@PathVariable("categoryId") Long categoryId,
			@Valid @ModelAttribute("category") CategoryRequest categoryRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "categoryAdmin/category-edit";
		}
		categoryService.updateCategory(categoryId, categoryRequest, bindingResult);
		return "redirect:/admin/categories";
	}
}
