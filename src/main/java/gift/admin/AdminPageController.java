package gift.admin;

import gift.category.CategoryService;
import gift.member.MemberService;
import gift.product.ProductService;
import gift.product.dto.ProductPaginationResponseDTO;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final MemberService memberService;

    public AdminPageController(
        ProductService productService,
        CategoryService categoryService,
        MemberService memberService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.memberService = memberService;
    }

    @GetMapping
    public String adminPage(
        Model model,
        Pageable pageable
    ) {
        Page<ProductPaginationResponseDTO> products = productService.getAllProducts(pageable);

        model.addAttribute("products", products);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("totalProductsSize", products.getTotalElements());
        model.addAttribute("currentPageProductSize", products.get().toList().size());
        model.addAttribute("pageLists",
            IntStream.range(0, products.getTotalPages()).boxed().toList());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "/product/page";
    }

    @GetMapping("/member")
    public String memberPage(
        Model model
    ) {
        model.addAttribute("members", memberService.getAllMembers());
        return "/member/page";
    }
}
