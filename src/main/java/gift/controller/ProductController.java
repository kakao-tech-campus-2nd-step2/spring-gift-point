package gift.controller;

import gift.dto.ProductRequest;
import gift.exception.InvalidProductException;
import gift.exception.ProductNotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private final ProductService productService;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @ApiOperation(value = "Get all products", notes = "Retrieve all products with pagination")
    public String getProducts(@ApiParam(value = "Page number", required = false, defaultValue = "0") @RequestParam(defaultValue = "0") int page,
        @ApiParam(value = "Page size", required = false, defaultValue = "5") @RequestParam(defaultValue = "5") int size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getAllProducts(pageable);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("productPage", productPage);
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categories);
        return "product-list";
    }

    @PostMapping
    @ResponseBody
    public String addProduct(@RequestBody ProductRequest productRequest) {
        Product product = productService.addProduct(productRequest);
        optionService.addOption(productRequest.dtoToOptionEntity(product));

        return "redirect:/api/products";
    }

    @PutMapping("/{product_id}")
    @ResponseBody
    public String updateProduct(@PathVariable("product_id") Long id,@RequestBody ProductRequest productRequest,
        RedirectAttributes redirectAttributes) {

        Product oldProduct = productService.getProductById(id).get();
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
        Product product1 = new Product(category, productRequest);
        product1.setOptions(oldProduct.getOptions());
        productService.updateProduct(id, product1);

        redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
        return "redirect:/products";
    }

    @DeleteMapping("/{product_id}")
    @ResponseBody
    public String deleteProduct(@PathVariable("product_id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
/*
    @GetMapping("/view/{id}")
    @ApiOperation(value = "Get product details", notes = "Retrieve the details of a specific product by ID")
    public String getProductDetails(@ApiParam(value = "Product ID", required = true) @PathVariable("id") Long id, Model model,
        RedirectAttributes redirectAttributes) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("product", product.get());
            model.addAttribute("categories", categories);
            return "product-detail"; // product-details.html 뷰 반환
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
            return "redirect:/products";
        }
    }
*/
    @GetMapping("/{product_id}")
    public ResponseEntity<?> getProductById(@PathVariable("product_id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}