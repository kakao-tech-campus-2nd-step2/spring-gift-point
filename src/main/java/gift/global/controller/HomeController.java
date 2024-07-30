package gift.global.controller;

import gift.domain.category.Category;
import gift.domain.category.CategoryService;
import gift.domain.product.Product;
import gift.domain.product.ProductService;
import gift.domain.product.dto.response.ProductPageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /**
     * 홈 화면 렌더링 (thymeleaf)
     */
    @GetMapping
    String homePage(
        Model model,
        @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "0") int page,
        @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "10") int size,
        @Parameter(description = "정렬 기준") @RequestParam(value = "sort", defaultValue = "id_asc") String sort,
        @Parameter(description = "카테고리 ID") @RequestParam(value = "categoryId", defaultValue = "0") Long categoryId
    ) {
        Sort sortObj = getSortObject(sort);
        ProductPageResponse productPageResponse = productService.getProductsByPage(page, size,
            sortObj, categoryId);
        List<Category> categories = categoryService.getCategories();

        model.addAttribute("products", productPageResponse.products());
        model.addAttribute("categories", categories);
        // 성공 시
        return "index";
    }

    private Sort getSortObject(String sort) {
        switch (sort) {
            case "price_asc":
                return Sort.by(Sort.Direction.ASC, "price");
            case "price_desc":
                return Sort.by(Sort.Direction.DESC, "price");
            case "name_asc":
                return Sort.by(Sort.Direction.ASC, "name");
            case "name_desc":
                return Sort.by(Sort.Direction.DESC, "name");
            default:
                return Sort.by(Sort.Direction.ASC, "id");
        }
    }

}
