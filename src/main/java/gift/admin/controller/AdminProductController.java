package gift.admin.controller;

import gift.admin.dto.LeafProductDTO;
import gift.product.dto.ProductRequest;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/products")
@Controller
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("products", productService.readAll());
        model.addAttribute("product", new LeafProductDTO());
        return "productIndex";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("product", new LeafProductDTO());
        return "admin/createProduct";
    }

    @PostMapping("/new")
    public String createPageSubmit(
        @ModelAttribute("leafProductDTO") @Valid LeafProductDTO leafProductDTO) {

        ProductRequest productRequest = toRestRequest(leafProductDTO);

        productService.create(productRequest);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}")
    public String updatePage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.read(id));
        return "admin/updateProduct";
    }

    @PutMapping("/{id}")
    public String updatePageSubmit(@PathVariable Long id,
        @ModelAttribute("leafProductDTO") @Valid LeafProductDTO leafProductDTO) {

        ProductRequest productRequest = toRestRequest(leafProductDTO);

        productService.update(id, productRequest);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String deletePageSubmit(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    private ProductRequest toRestRequest(LeafProductDTO leafProductDTO) {
        return new ProductRequest(leafProductDTO.getId(), leafProductDTO.getName(),
            leafProductDTO.getPrice(), leafProductDTO.getImageUrl(), leafProductDTO.getCategoryId(),
            leafProductDTO.getGiftOptionName(), leafProductDTO.getGiftOptionQuantity());
    }

}
