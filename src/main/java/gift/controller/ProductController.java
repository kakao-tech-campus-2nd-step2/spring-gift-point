package gift.controller;

import gift.exceptions.CustomException;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import gift.dto.*;


@Controller
@Validated
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllProducts(Model model, @PageableDefault(size = 3) Pageable pageable) {
        ProductsPageResponseDTO products = productService.getAllProducts(pageable);
        CategoryResponseDTO categories = categoryService.getAllCategories();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "manage";
    }

    @PostMapping
    public String addProduct(@Valid  @RequestBody ProductRequestDTO productRequestDTO) {
        validateProductName(productRequestDTO.name());
        productService.createProduct(productRequestDTO);

        return "redirect:/api/products";
    }

    @PutMapping("/{id}")
    public String modifyProduct(@PathVariable(name = "id") Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        validateProductName(productRequestDTO.name());
        productService.updateProduct(id, productRequestDTO);

        return "redirect:/api/products";
    }

    @DeleteMapping("/{id}")
    public String DeleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);

        return "redirect:/api/products";
    }

    private void validateProductName(String name) {
        if (name.contains("카카오")) {
            throw CustomException.invalidNameException();
        }
    }
}