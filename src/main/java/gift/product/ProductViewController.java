package gift.product;

import gift.category.CategoryResponse;
import gift.category.CategoryService;
import gift.option.OptionRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ProductViewController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductViewController(ProductService productService, CategoryService categoryService) {
        this.categoryService = categoryService;
        ;
        this.productService = productService;
    }

    @ModelAttribute("categories")
    public List<CategoryResponse> categories() {
        return categoryService.findAllCategories();
    }

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/manager/products";
    }

    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute("newProduct") ProductRequest newProduct, BindingResult bindingResult1, @Valid @ModelAttribute("option") OptionRequest option, BindingResult bindingResult2, RedirectAttributes redirectAttributes) {
        if (bindingResult1.hasErrors() || bindingResult2.hasErrors()) {
            return "AddProduct";
        }

        Product product = productService.insertNewProduct(newProduct, option);
        redirectAttributes.addAttribute("id", product.getId());

        return "redirect:/manager/products/{id}";
    }

    @PutMapping("/products/update/{id}")
    @Transactional
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") ProductRequest product, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "UpdateProduct";
        }
        productService.updateProduct(id, product);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/manager/products/{id}";
    }

    @GetMapping("/products")
    public String getProductsView(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "ManageProduct";
    }

    @GetMapping("/products/add")
    public String addProductView(Model model) {
        model.addAttribute("newProduct", new ProductResponse());
        model.addAttribute("option", new OptionRequest());
        return "AddProduct";
    }

    @GetMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", new ProductResponse(productService.findByID(id)));
        return "UpdateProduct";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable long id, Model model) {
        model.addAttribute("product", new ProductResponse(productService.findByID(id)));
        return "ProductInfo";
    }
}
