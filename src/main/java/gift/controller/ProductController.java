package gift.controller;

import gift.dto.OptionDTO;
import gift.dto.PageRequestDTO;
import gift.dto.ProductDTO;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
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
@RequestMapping("/admin/products")
@Validated
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductController(ProductService productService, CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "This API retrieves all products with pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved products."),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String allProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        Model model) {

        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, size, sortBy, direction);
        Page<ProductDTO> productPage = productService.findAllProducts(pageRequestDTO);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "Products";
    }

    @GetMapping("/add")
    @Operation(summary = "Get add product form", description = "This API returns the form to add a new product.")
    public String addProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "Add_product";
    }

    @PostMapping
    @Operation(summary = "Add a new product", description = "This API adds a new product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully added product."),
        @ApiResponse(responseCode = "400", description = "Invalid product data."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String addProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllCategories());
            return "Add_product";
        }
        productService.addProduct(productDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Get edit product form", description = "This API returns the form to edit an existing product.")
    public String editProductForm(@PathVariable Long id, Model model) {
        Optional<ProductDTO> productDTO = productService.findProductById(id);
        if (productDTO.isEmpty()) {
            return "redirect:/admin/products";
        }
        model.addAttribute("product", productDTO.get());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "Edit_product";
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product", description = "This API updates an existing product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated product."),
        @ApiResponse(responseCode = "400", description = "Invalid product data."),
        @ApiResponse(responseCode = "404", description = "Product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String updateProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllCategories());
            return "Edit_product";
        }
        productService.updateProduct(productDTO);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "This API deletes an existing product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted product."),
        @ApiResponse(responseCode = "404", description = "Product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/options")
    @Operation(summary = "Manage product options", description = "This API returns the form to manage options for a product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product options."),
        @ApiResponse(responseCode = "404", description = "Product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String manageProductOptions(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.findProductById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        List<OptionDTO> productOptions = productDTO.getOptions();
        List<OptionDTO> allOptions = optionService.findAllOptions();

        model.addAttribute("product", productDTO);
        model.addAttribute("options", allOptions.stream()
            .filter(option -> productOptions.stream().anyMatch(productOption -> productOption.getId().equals(option.getId())))
            .collect(Collectors.toList()));

        return "Manage_product_options";
    }

    @PostMapping("/{id}/options")
    @Operation(summary = "Update product options", description = "This API updates the options for a product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated product options."),
        @ApiResponse(responseCode = "404", description = "Product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String updateProductOptions(@PathVariable Long id, @RequestParam List<Long> optionIds) {
        productService.updateProductOptions(id, optionIds);
        return "redirect:/admin/products";
    }
}