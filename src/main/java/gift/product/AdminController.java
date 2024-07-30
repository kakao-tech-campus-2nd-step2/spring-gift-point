package gift.product;

import gift.product.model.ProductRequest;
import gift.product.model.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(Model model,
        @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
        Page<ProductResponse> productList = productService.getAllProducts(pageable);
        model.addAttribute("productList", productList);
        return "products";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        return "add-product-form";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductRequest.Create productCreate,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-product-form";
        }
        productService.insertProduct(productCreate);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("productRequestDto",
            ProductRequest.Update.from(productService.getProductById(id)));
        return "modify-product-form";
    }

    @PostMapping("edit/{id}")
    public String updateProduct(@PathVariable("id") Long id,
        @Valid @ModelAttribute ProductRequest.Update productUpdate,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "modify-product-form";
        }
        productService.updateProductById(id, productUpdate);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }

}
