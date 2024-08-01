package gift.product.controller;

import gift.product.dto.ProductDTO;
import gift.product.model.Category;
import gift.product.service.CategoryService;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public AdminProductController(
        ProductService productService,
        CategoryService categoryService
    ) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showProductList(Model model, Pageable pageable) {
        System.out.println("[ProductController] showProductList()");
        model.addAttribute("productList", productService.getAllProducts(pageable));
        return "product-management-list";
    }

    @GetMapping("/register")
    public String showProductForm(Model model) {
        System.out.println("[ProductController] showProductForm()");
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDTO());
        return "product-form";
    }

    @PostMapping
    public String registerProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult bindingResult, Model model) {
        System.out.println("[ProductController] registerProduct()");
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productDTO);
            return "product-form";
        }
        productService.registerProduct(productDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}")
    public String updateProductForm(@PathVariable Long id, Model model) {
        System.out.println("[ProductController] updateProductForm()");
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        ProductDTO productDTO = productService.getDTOById(id);
        model.addAttribute("product", productDTO);
        return "product-update-form";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductDTO productDTO, BindingResult bindingResult, Model model) {
        System.out.println("[ProductController] updateProduct()");
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productDTO);
            return "product-form";
        }
        productService.updateProduct(id, productDTO);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        System.out.println("[ProductController] deleteProduct()");
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model, Pageable pageable) {
        System.out.println("[ProductController] searchProduct()");
        model.addAttribute("searchResults", productService.searchProducts(keyword, pageable));
        model.addAttribute("keyword", keyword);
        return "product-search-list";
    }
}
