package gift.domain.product.controller;

import gift.domain.product.dto.ProductRequest;
import gift.domain.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(
        Model model,
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        model.addAttribute("products", productService.getAllProducts(pageNo, pageSize));
        return "index";
    }

    @GetMapping("/create-product")
    public String createPage() {
        return "create";
    }

    @PostMapping("/create-product")
    public String create(@ModelAttribute @Valid ProductRequest productRequest) {

        productService.createProduct(productRequest);
        return "redirect:/";
    }

    @GetMapping("/update-product/{id}")
    public String updatePage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "update";
    }

    @PostMapping("/update-product/{id}")
    public String update(@PathVariable("id") Long id,
        @ModelAttribute @Valid ProductRequest productRequest) {

        productService.updateProduct(id, productRequest);
        return "redirect:/";
    }

    @GetMapping("/delete-product/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String handleNameException(MethodArgumentNotValidException e, Model model) {
        List<String> errorMessages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errorMessages.add(errorMessage);
        });
        model.addAttribute("errorMessages", errorMessages);
        return "error";
    }
}
