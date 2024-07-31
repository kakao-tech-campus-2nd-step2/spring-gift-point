package gift.product.controller;

import gift.category.service.CategoryService;
import gift.product.domain.ProductRequest;
import gift.product.domain.ProductResponse;
import gift.product.option.service.OptionService;
import gift.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/products")
public class ProductViewController
{
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductViewController(ProductService productService, CategoryService categoryService,
        OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping("")
    public String getAllProducts(Model model, @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPages = productService.getAllProducts(pageable);
        model.addAttribute("products", productPages);
        return "products";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("categories", categoryService.findAll());
        return "add_product";
    }

    @PostMapping("")
    public String createProduct(@ModelAttribute ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{productId}")
    public String showEditForm(@PathVariable("productId") Long productId, Model model) {
        ProductRequest productRequest = productService.getProductRequestById(productId)
            .orElseThrow(() -> new IllegalArgumentException("productId " + productId + "가 없습니다."));
        model.addAttribute("product", productRequest);
        model.addAttribute("categories", categoryService.findAll());
        return "edit_product";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
