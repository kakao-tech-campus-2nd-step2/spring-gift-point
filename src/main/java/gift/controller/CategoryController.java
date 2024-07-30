package gift.controller;

import gift.dto.CategoryDTO;
import gift.dto.PageRequestDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/categories")
@Validated
@Tag(name = "Category Management", description = "APIs for managing categories")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "This API retrieves all categories with pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved categories."),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<List<CategoryDTO>> allCategories(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        Model model) {

        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, size, sortBy, direction);
        Page<CategoryDTO> categoryPage = categoryService.findAllCategories(pageRequestDTO);

        List<CategoryDTO> categoriesDto = categoryPage.getContent().stream()
            .map(cate -> new CategoryDTO(
                cate.getId(),
                cate.getName(),
                cate.getColor(),
                cate.getImageUrl(),
                cate.getDescription()
            ))
            .collect(Collectors.toList());

        return new ResponseEntity<>(categoriesDto, HttpStatus.OK);
    }

    @GetMapping("/add")
    @Operation(summary = "Get add category form", description = "This API returns the form to add a new category.")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "Add_category";
    }

    @PostMapping
    @Operation(summary = "Add a new category", description = "This API adds a new category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully added category."),
        @ApiResponse(responseCode = "400", description = "Invalid category data."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String addCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "Add_category";
        }
        categoryService.addCategory(categoryDTO);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Get edit category form", description = "This API returns the form to edit an existing category.")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Optional<CategoryDTO> categoryDTO = categoryService.findCategoryById(id);
        if (categoryDTO.isEmpty()) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", categoryDTO.get());
        return "Edit_category";
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category", description = "This API updates an existing category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated category."),
        @ApiResponse(responseCode = "400", description = "Invalid category data."),
        @ApiResponse(responseCode = "404", description = "Category not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String updateCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "Edit_category";
        }
        categoryService.updateCategory(categoryDTO);
        return "redirect:/admin/categories";
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "This API deletes an existing category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted category."),
        @ApiResponse(responseCode = "404", description = "Category not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}