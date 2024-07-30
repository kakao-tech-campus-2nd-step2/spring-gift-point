package gift.member.api;

import gift.global.pagination.dto.PageResponse;
import gift.member.validator.LoginMember;
import gift.product.api.ProductController;
import gift.product.dto.ProductResponse;
import gift.wishlist.api.WishesController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberViewController {

    private final ProductController productController;
    private final WishesController wishesController;

    public MemberViewController(ProductController productController,
                                WishesController wishesController) {
        this.productController = productController;
        this.wishesController = wishesController;
    }

    @GetMapping("/register")
    public String showSignupView() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginView() {
        return "login";
    }

    @GetMapping("/wishlist")
    public String showWishlistView(@LoginMember Long memberId,
                                   Model model,
                                   @PageableDefault(
                                           sort = "id",
                                           direction = Sort.Direction.DESC)
                                   Pageable pageable) {
        Page<ProductResponse> products = productController.getPagedProducts(pageable);
        Page<ProductResponse> wishes = wishesController.getPagedWishes(memberId, pageable);

        model.addAttribute("productList", products.getContent());
        model.addAttribute("productPageInfo", new PageResponse(products));
        model.addAttribute("wishlist", wishes.getContent());
        model.addAttribute("wishlistPageInfo", new PageResponse(wishes));
        return "wishlist";
    }

    @GetMapping("/order")
    public String showOrderView(Model model,
                                @PageableDefault(
                                        sort = "id",
                                        direction = Sort.Direction.DESC)
                                Pageable pageable) {
        Page<ProductResponse> products = productController.getPagedProducts(pageable);
        model.addAttribute("productList", products.getContent());
        model.addAttribute("pageInfo", new PageResponse(products));
        return "order";
    }

}
