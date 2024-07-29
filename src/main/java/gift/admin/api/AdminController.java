package gift.admin.api;

import gift.product.api.CategoryController;
import gift.global.pagination.dto.PageResponse;
import gift.product.api.ProductController;
import gift.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductController productController;

    private final CategoryController categoryController;

    public AdminController(ProductController productController, CategoryController categoryController) {
        this.productController = productController;
        this.categoryController = categoryController;
    }

    // 상품 조회
    @GetMapping
    public String getAllProducts(Model model,
                                 @PageableDefault(
                                         sort = "id",
                                         direction = Sort.Direction.DESC)
                                 Pageable pageable) {
        Page<ProductResponse> pagedProducts = productController.getPagedProducts(pageable);
        PageResponse pageInfo = new PageResponse(pagedProducts);

        model.addAttribute("productList", pagedProducts.getContent());
        model.addAttribute("pageInfo", pageInfo);
        return "admin-product-list";
    }

    // 상품 추가 폼 표시
    @GetMapping("/add")
    public String showProductAddForm(Model model) {
        model.addAttribute("categoryList", categoryController.getAllCategories());
        return "product-add-form";
    }

    // 상품 수정 폼 표시
    @GetMapping("/edit/{id}")
    public String showProductEditForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("product", productController.getProduct(id));
        model.addAttribute("categoryList", categoryController.getAllCategories());
        return "product-edit-form";
    }

    // 상품 옵션 추가 폼 표시
    @GetMapping("/options/add/{id}")
    public String showOptionAddForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("productId", id);
        return "option-add-form";
    }

}
