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
@RequestMapping("/api/products")
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
    public ResponseEntity<List<ProductDTO>> allProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(required = false) Long categoryId) {

        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, size, sortBy, direction);
        Page<ProductDTO> productPage;

        if (categoryId != null) {
            productPage = productService.findProductsByCategory(categoryId, pageRequestDTO);
        } else {
            productPage = productService.findAllProducts(pageRequestDTO);
        }

        List<ProductDTO> productList = productPage.getContent();

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID", description = "This API retrieves a specific product by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product."),
        @ApiResponse(responseCode = "404", description = "Product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        Optional<ProductDTO> productDTO = productService.findProductById(productId);
        return productDTO
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
        return "redirect:/api/products";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Get edit product form", description = "This API returns the form to edit an existing product.")
    public String editProductForm(@PathVariable Long id, Model model) {
        Optional<ProductDTO> productDTO = productService.findProductById(id);
        if (productDTO.isEmpty()) {
            return "redirect:/api/products";
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
        return "redirect:/api/products";
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
        return "redirect:/api/products";
    }


    @GetMapping("/{productId}/options")
    @Operation(summary = "Get product options by product ID", description = "This API retrieves all options for a specific product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product options."),
        @ApiResponse(responseCode = "404", description = "Product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<List<OptionDTO>> getProductOptions(@PathVariable Long productId) {
        Optional<ProductDTO> productDTO = productService.findProductById(productId);
        if (productDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<OptionDTO> productOptions = productDTO.get().getOptions();
        return new ResponseEntity<>(productOptions, HttpStatus.OK);
    }
}