package gift.product.controller;

import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Tag(name = "admin-controller", description = "view 반환")
@RequestMapping("/admin")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    @Operation
    public String loadMainPage(@RequestParam(name = "page-no") int pageNumber,
        @RequestParam(name = "sorting-state") int sortingState,
        List<ProductResponseDto> products, Model model) {

        model.addAttribute("products", products);

        // 작업 후에 기존 상태를 보존해야 하므로 model에 넣었습니다.
        model.addAttribute("pageNo", pageNumber);
        model.addAttribute("sortingState", sortingState);

        return "html/admin";
    }

    // edit-product.html을 SSR로 넘겨주는 핸들러
    @GetMapping("/products/{product-id}/editor")
    @Operation
    public String loadProductEditPage(@PathVariable(name = "product-id") long productId,
        Model model) {
        ProductResponseDto product = productService.selectProduct(productId);

        model.addAttribute("product", product);
        return "html/edit-product";
    }

    @GetMapping("/users/manager")
    @Operation
    public String loadUserEditPage(Model model) {
        return "html/edit-user";
    }
}
