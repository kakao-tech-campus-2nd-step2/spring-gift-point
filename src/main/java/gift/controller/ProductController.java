package gift.controller;

import gift.dto.ProductPage;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ResponseEntity<?> getProducts(@RequestParam(defaultValue = "0") int page,
        @RequestParam Long category) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Product> productPage = productService.getProductsByCategoryId(pageable, category);
        ProductPage productPage1 = new ProductPage();
        productPage1.setContent(productPage.getContent());
        productPage1.setTotal_page(productPage.getTotalPages());

        Map<String, Object> dataWrapper = new HashMap<>();

        dataWrapper.put("data", productPage1);
        return ResponseEntity.status(HttpStatus.OK).body(dataWrapper);
    }

    @GetMapping("/list")
    @ResponseBody
    public String getAllProducts(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size, Model model) {
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

    @GetMapping("/view/{id}")
    @ResponseBody
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

    @GetMapping("/{product_id}")
    public ResponseEntity<?> getProductById(@PathVariable("product_id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}