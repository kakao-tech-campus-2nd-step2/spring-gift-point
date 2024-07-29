package gift.controller;

import gift.dto.ProductCreateRequest;
import gift.dto.ProductRequest;
import gift.entity.Category;
import gift.entity.Product;
import gift.entity.ProductFactory;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductFactory productFactory;

    public ProductAdminController(ProductService productService, CategoryService categoryService,
        ProductFactory productFactory) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productFactory = productFactory;
    }

    @GetMapping
    public String getAllProducts(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Product> productPage = productService.getAllProducts(pageable);
        model.addAttribute("productPage", productPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("productRequest", new ProductRequest());
        return "product-list";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("productCreateRequest", new ProductCreateRequest());
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Category> categoryPage = categoryService.getAllCategories(pageable);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("product", productFactory.createProduct());
        return "product-create";
    }

    @PostMapping("/add")
    public String addProduct(
        @Valid @ModelAttribute("productCreateRequest") ProductCreateRequest productCreateRequest,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
            Page<Category> categoryPage = categoryService.getAllCategories(pageable);
            model.addAttribute("categories", categoryPage.getContent());
            model.addAttribute("productCreateRequest", productCreateRequest);
            return "product-create";
        }
        productService.saveProduct(productCreateRequest);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        ProductRequest productRequest = new ProductRequest(
            product.getName(), product.getPrice(), product.getImg(), product.getCategory().getId());
        model.addAttribute("productRequest", productRequest);
        return getString(model, product);
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id,
        @Valid @ModelAttribute("productRequest") ProductRequest productRequest,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Product product = productService.getProductById(id);
            return getString(model, product);
        }
        productService.updateProduct(id, productRequest);
        return "redirect:/admin/products";
    }

    private String getString(Model model, Product product) {
        model.addAttribute("product", product);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Category> categoryPage = categoryService.getAllCategories(pageable);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("productOptions", product.getOptions());
        return "product-edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
