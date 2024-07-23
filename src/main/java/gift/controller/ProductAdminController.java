package gift.controller;

import gift.dto.CategoryDto;
import gift.dto.PageRequestDto;
import gift.dto.ProductRegisterRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductAdminController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getPagedProducts(Model model, @Valid PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.toPageable();
        Page<ProductResponseDto> productPage = productService.getPagedProducts(pageable);
        model.addAttribute("products", productPage);
        model.addAttribute("currentPage", pageRequestDto.getPageNumber());
        model.addAttribute("sortBy", pageRequestDto.getSortBy());
        return "admin";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductRegisterRequestDto());
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductRegisterRequestDto productDto) {
        productService.addProduct(productDto);
        return "redirect:/admin/products";
    }

    @GetMapping("/update/{id}")
    public String showUpdateProductForm(@PathVariable("id") Long id, Model model) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID는 null, 0, 음수는 불가입니다.");
        }
        ProductResponseDto productDto = productService.getProductById(id);
        model.addAttribute("product", productDto);
        model.addAttribute("productId", id);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute ProductRegisterRequestDto productDto) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID는 null, 0, 음수는 불가입니다.");
        }
        productService.updateProduct(id, productDto);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID는 null, 0, 음수는 불가입니다.");
        }
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
