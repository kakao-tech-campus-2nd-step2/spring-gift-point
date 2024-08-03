package gift.admin;

import gift.domain.member.service.MemberService;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductResponse;
import gift.domain.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AdminController {

    private final ProductService productService;
    private final MemberService memberService;

    public AdminController(ProductService productService, MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String index(Model model, @ParameterObject Pageable pageable) {
        Page<ProductResponse> productPage = productService.getAllProducts(pageable, null);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        model.addAttribute("currentPage", productPage.getNumber() + 1);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "index";
    }

    @GetMapping("/products/create")
    public String getCreatePage() {
        return "create";
    }

    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute @Valid ProductRequest productRequest) {

        productService.createProduct(productRequest);
        return "redirect:/";
    }

    @GetMapping("/products/update/{id}")
    public String getUpdatePage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "update";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
        @ModelAttribute @Valid ProductRequest productRequest) {

        productService.updateProduct(id, productRequest);
        return "redirect:/";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String getMemberPage(
        Model model,
        @ParameterObject Pageable pageable
    ) {
        model.addAttribute("members", memberService.getAllMember(pageable));
        return "member";
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
