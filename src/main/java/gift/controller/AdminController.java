package gift.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import gift.dto.CategoryUpdateRequest;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.dto.ProductUpdateRequest;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class AdminController {

    private final CategoryService categoryService;
    private final ProductService productService;
    
    public AdminController(CategoryService categoryService, ProductService productService) {
    	this.categoryService =categoryService;
    	this.productService = productService;
    }

    @GetMapping
    public String adminPage(Model model,
    		@PageableDefault(sort="name") Pageable pageable) {
        Page<ProductResponse> productList = productService.getProducts(pageable);
        model.addAttribute("products", productList);
        return "admin";
    }

    @GetMapping("/new")
    public String addProductForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", "교환권"));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-form";
    }

    @PostMapping
    public String addProduct(@ModelAttribute @Valid ProductRequest request, BindingResult bindingResult) {
        productService.createProduct(request, bindingResult);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
    	ProductResponse product = productService.getProduct(id);
        if (product == null) {
            return "redirect:/admin/products";
        }

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(product.getName(), 
        		product.getPrice(), product.getImageUrl());
        model.addAttribute("productUpdateRequest", productUpdateRequest);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-form";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, @ModelAttribute @Valid ProductUpdateRequest request, 
    		BindingResult bindingResult) {
        productService.updateProduct(id, request, bindingResult);
        return "redirect:/admin/products";
    }
    
    @PutMapping("/edit/{id}/catetory")
    public String updateProductCategory(@PathVariable("id") Long id, @ModelAttribute @Valid CategoryUpdateRequest request, 
    		BindingResult bindingResult) {
    	productService.updateProductCategory(id, request, bindingResult);
    	return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
