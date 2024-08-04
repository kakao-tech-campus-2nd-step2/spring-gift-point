package gift.product.presentation;

import gift.product.application.ProductService;
import gift.product.application.ProductServiceResponse;
import gift.product.presentation.request.ProductCreateRequest;
import gift.product.presentation.request.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductViewController {
    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public String findAll(Pageable pageable, Model model) {
        Page<ProductServiceResponse> products = productService.findAll(pageable);
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long productId, Model model) {
        ProductServiceResponse product = productService.findById(productId);
        model.addAttribute("product", product);
        return "product/detail";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("productCreateRequest", new ProductCreateRequest("", 0, "", 0L, null));
        return "product/create";
    }

    @PostMapping("")
    public String create(@ModelAttribute ProductCreateRequest request) {
        productService.save(request.toCommand());
        return "redirect:/products";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") Long productId, Model model) {
        ProductServiceResponse product = productService.findById(productId);
        model.addAttribute("product", product);
        return "product/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long productId, @ModelAttribute ProductUpdateRequest request) {
        productService.update(request.toCommand(productId));
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long productId) {
        productService.delete(productId);
        return "redirect:/products";
    }
}
