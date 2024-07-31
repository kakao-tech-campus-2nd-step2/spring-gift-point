package gift.admin;

import gift.category.CategoryService;
import gift.product.ProductService;
import gift.product.dto.ProductPaginationResponseDTO;
import gift.product.dto.ProductResponseDTO;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminPageController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminPageController(
        ProductService productService,
        CategoryService categoryService
    ) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Deprecated
    @GetMapping(path = "/admin/{categoryId}")
    public String adminPage(
        Model model,
        Pageable pageable,
        @PathVariable long categoryId
    ) {
        Page<ProductPaginationResponseDTO> products = productService.getAllProducts(pageable, categoryId);

        model.addAttribute("products", products);
        model.addAttribute("page", pageable.getPageNumber() + 1);
        model.addAttribute("totalProductsSize", products.getTotalElements());
        model.addAttribute("currentPageProductSize", products.get().toList().size());
        model.addAttribute("pageLists",
            IntStream.range(1, products.getTotalPages() + 1).boxed().toList());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "/product/page";
    }
}
