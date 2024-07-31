package gift.controller.admin;

import gift.controller.dto.request.ProductRequest;
import gift.controller.dto.response.CategoryResponse;
import gift.controller.dto.response.PagingResponse;
import gift.controller.dto.response.ProductResponse;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String getProducts(Model model,
              @PageableDefault(size = 10) Pageable pageable) {
        PagingResponse<ProductResponse.WithOption> products = productService.findAllProductPaging(pageable);
        model.addAttribute("products", products);
        return "product/products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        CategoryResponse.InfoList categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "product/newProduct";
    }

    @GetMapping("/{id}")
    public String updateProduct(@PathVariable("id") @NotNull @Min(1) Long id, Model model) {
        ProductResponse.WithOption product = productService.findById(id);
        CategoryResponse.InfoList categories = categoryService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product/editProduct";
    }

    @PostMapping("")
    public String createProduct(@Valid @ModelAttribute ProductRequest.AdminCreateProduct request) {
        productService.save(request.toDto());
        return "redirect:/admin/product";
    }

    @PutMapping("/{id}")
    public String updateProduct(@Valid @ModelAttribute ProductRequest.AdminUpdateProduct request) {
        productService.updateProduct(request.toDto());
        return "redirect:/admin/product";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") @NotNull @Min(1) Long id) {
        productService.deleteById(id);
        return "redirect:/admin/product";
    }
}
