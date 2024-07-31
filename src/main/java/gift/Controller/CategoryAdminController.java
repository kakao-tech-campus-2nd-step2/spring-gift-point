package gift.Controller;

import gift.DTO.CategoryDto;
import gift.Service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {

  private final CategoryService categoryService;

  public CategoryAdminController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public String listCategories(Model model) {
    model.addAttribute("categories", categoryService.getAllCategories());

    return "category-list";
  }

  @GetMapping("/new")
  public String newCategoryForm(Model model) {
    model.addAttribute("category", new CategoryDto());

    return "category-form";
  }

  @PostMapping("/add")
  public String addCategory(@RequestParam String name, @RequestParam String color,
    @RequestParam String imageUrl, @RequestParam String description) {
    CategoryDto categoryDto = new CategoryDto(name, color, imageUrl, description);
    categoryService.addCategory(categoryDto);

    return "redirect:/admin/categories";
  }

  @GetMapping("/category/{id}")
  public String editCategoryForm(@PathVariable Long id, Model model) {
    CategoryDto categoryDto = categoryService.getCategoryById(id);
    model.addAttribute("category", categoryDto);

    return "category-form";
  }

  @PostMapping("/category/{id}")
  public String updateCategory(@PathVariable Long id, @ModelAttribute CategoryDto categoryDto) {
    categoryService.updateCategory(id, categoryDto);

    return "redirect:/admin/categories";
  }

  @PostMapping("/{id}")
  public String delteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);

    return "redirect:/admin/categories";
  }
}
