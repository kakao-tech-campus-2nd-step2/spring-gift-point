package gift.controller.admin;

import gift.dto.request.AddProductRequest;
import gift.dto.request.UpdateProductRequest;
import gift.entity.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductAdminController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductAdminController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/product")
    public String getProducts(Model model, @PageableDefault(sort = "id") Pageable pageable) {
        model.addAttribute("products", productService.getProductResponses(pageable));
        return "version-SSR/index";
    }

    @GetMapping("/add")
    public String getAddForm(Model model) {
        model.addAttribute("addProductRequest", new AddProductRequest("", 0, "", 0L, new ArrayList<>()));
        model.addAttribute("categories", categoryService.getAllCategoryResponses());
        return "version-SSR/add-form";
    }

    @PostMapping("/add")
    public String addProduct(@Valid AddProductRequest request) {
        try {
            productService.addProduct(request);
            return "redirect:/product";
        } catch (Exception e) {
            return "version-SSR/add-error";
        }
    }

    @PostMapping("/deleteSelected")
    public String deleteSelectedProduct(@RequestParam("productIds") List<Long> productIds) {
        productService.deleteProducts(productIds);
        return "redirect:/product";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("productId") Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable("id") long id, Model model) {
        Product existingProduct = productService.getProduct(id);
        model.addAttribute("updateProductRequest", new UpdateProductRequest(existingProduct.getName(), existingProduct.getPrice(), existingProduct.getImageUrl(), existingProduct.getId()));
        model.addAttribute("categories", categoryService.getAllCategoryResponses());
        model.addAttribute("productId", id);
        return "version-SSR/edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@Valid UpdateProductRequest request, @PathVariable("id") Long productId) {
        try {
            productService.updateProduct(request, productId);
            return "redirect:/product";
        } catch (Exception e) {
            return "version-SSR/edit-error";
        }
    }
}
