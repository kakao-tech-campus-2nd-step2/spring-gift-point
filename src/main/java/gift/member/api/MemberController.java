package gift.member.api;

import gift.auth.application.AuthService;
import gift.global.pagination.dto.PageResponse;
import gift.product.dto.ProductResponse;
import gift.wishlist.api.WishesController;
import gift.member.validator.LoginMember;
import gift.member.application.MemberService;
import gift.auth.dto.AuthResponse;
import gift.member.dto.MemberDto;
import gift.product.api.ProductController;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final ProductController productController;
    private final WishesController wishesController;

    public MemberController(MemberService memberService,
                            AuthService authService,
                            ProductController productController,
                            WishesController wishesController) {
        this.memberService = memberService;
        this.authService = authService;
        this.productController = productController;
        this.wishesController = wishesController;
    }

    @GetMapping("/register")
    public String showSignupView() {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public void signUp(@RequestBody @Valid MemberDto memberDto) {
        memberService.registerMember(memberDto);
    }

    @GetMapping("/login")
    public String showLoginView() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AuthResponse login(@RequestBody @Valid MemberDto memberDto) {
        return authService.authenticate(memberDto);
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
