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

@Controller
@RequestMapping("/products")
@Api(tags = "Product Management")
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

    @PostMapping("/add")
    @ApiOperation(value = "Add a new product", notes = "Creates a new product with the given details")
    public String addProduct(@ApiParam(value = "Product name", required = true) @RequestParam String name,
        @ApiParam(value = "Product price", required = true) @RequestParam int price,
        @ApiParam(value = "Product image URL", required = true) @RequestParam String imageUrl,
        @ApiParam(value = "Category ID", required = true) @RequestParam Long categoryId,
        @ApiParam(value = "Option names", required = true) @RequestParam List<String> optionNames,
        @ApiParam(value = "Option quantities", required = true) @RequestParam List<Long> optionQuantities,
        RedirectAttributes redirectAttributes) {
        Category category = categoryService.getCategoryById(categoryId);
        Product product = new Product(name, price, imageUrl, category);
        productService.addProduct(product);

        List<Option> options = new ArrayList<>();
        for (int i = 0; i < optionNames.size(); i++) {
            String optionName = optionNames.get(i);
            Long optionQuantity = optionQuantities.get(i);
            Option option = new Option(optionName, optionQuantity, product);
            options.add(option);
        }
        optionService.addOption(options);

        redirectAttributes.addFlashAttribute("message", "Product added successfully!");
        return "redirect:/products";
    }

    @PostMapping("/update")
    @ApiOperation(value = "Update an existing product", notes = "Updates the details of an existing product")
    public String updateProduct(@ApiParam(value = "Product name", required = true) @RequestParam String name,
        @ApiParam(value = "Product price", required = true) @RequestParam int price,
        @ApiParam(value = "Product image URL", required = true) @RequestParam String imageUrl,
        @ApiParam(value = "Category ID", required = true) @RequestParam Long categoryId,
        @ModelAttribute Product product,
        @ModelAttribute Option option,
        @ApiParam(value = "Option names", required = true) @RequestParam("optionNames") List<String> optionNames,
        @ApiParam(value = "Option quantities", required = true) @RequestParam("optionQuantities") List<Long> optionQuantities,
                                RedirectAttributes redirectAttributes) {

        List<Option> options = new ArrayList<>();
        Category category = categoryService.getCategoryById(categoryId);
        Product product1 = new Product(name, price, imageUrl, category);
        for (int i = 0; i < optionNames.size(); i++) {
            Option newOption = new Option(optionNames.get(i), optionQuantities.get(i), product1);
            options.add(newOption);
        }

        options.stream().forEach(eachOptions -> optionService.updateOption(option.getId(),eachOptions));



        product1.setOptions(options);
        productService.updateProduct(product.getId(), product1);

        redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "Delete a product", notes = "Deletes the product with the given ID")
    public String deleteProduct(@ApiParam(value = "Product ID", required = true) @PathVariable("id") Long id,
        RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        return "redirect:/products";
    }

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

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a product by ID", notes = "Retrieve a specific product by its ID")
    public ResponseEntity<Product> getProductById(@ApiParam(value = "Product ID", required = true) @PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}