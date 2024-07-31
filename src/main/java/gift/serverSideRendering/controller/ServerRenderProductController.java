package gift.serverSideRendering.controller;

import gift.domain.dto.request.OptionAddRequest;
import gift.domain.dto.request.ProductAddRequest;
import gift.domain.dto.request.ProductUpdateRequest;
import gift.domain.dto.response.ProductResponse;
import gift.domain.entity.Product;
import gift.domain.service.ProductService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ServerRenderProductController {

    private final ProductService service;

    public ServerRenderProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public String showProducts(Model model) {
        List<ProductResponse> products = service.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        //TODO: category ID, options를 처리할 수 있게 수정하기
        model.addAttribute("productRequestDto", new ProductAddRequest("", 0, "", null, new ArrayList<>()));
        return "addProduct";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductAddRequest requestDto, Model model) {
        service.addProduct(requestDto);
        return "redirect:/products";
    }

    @GetMapping("/update/{id}")
    public String showUpdateProductForm(@PathVariable("id") Long id, Model model) {
        //TODO: Product를 직접참조 안하게 수정하기
        Product product = service.getProductById(id);
        ProductResponse productResponse = ProductResponse.of(product);
        //TODO: category ID, options를 처리할 수 있게 수정하기
        ProductAddRequest dto = new ProductAddRequest(product.getName(), product.getPrice(), product.getImageUrl(), 0L, OptionAddRequest.of(product.getOptions()));
        model.addAttribute("productRequestDto", dto);
        model.addAttribute("productId", id);
        return "updateProduct";
    }

    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute ProductUpdateRequest updateRequestDto) {
        service.updateProductById(id, updateRequestDto);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        service.deleteProduct(id);
        return "redirect:/products";
    }
}
