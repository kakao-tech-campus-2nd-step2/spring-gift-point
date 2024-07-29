package gift.product.api;

import gift.global.pagination.dto.PageResponse;
import gift.product.application.ProductService;
import gift.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products/list")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProductListView(Model model,
                                     @PageableDefault(
                                             sort = "id",
                                             direction = Sort.Direction.DESC)
                                     Pageable pageable) {
        Page<ProductResponse> products = productService.getPagedProducts(pageable);

        model.addAttribute("productList", products.getContent());
        model.addAttribute("productPageInfo", new PageResponse(products));
        return "product-list";
    }

}
