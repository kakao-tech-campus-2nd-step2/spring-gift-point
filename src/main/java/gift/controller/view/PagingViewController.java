package gift.controller.view;

import static gift.paging.PagingService.PRODUCTS_PER_PAGE;
import static gift.paging.PagingService.SHOWING_PAGE_COUNT;
import static gift.paging.PagingService.WISH_PER_PAGE;

import gift.auth.CheckRole;
import gift.auth.JwtService;
import gift.auth.LoginMember;
import gift.exception.wishlist.WishException;
import gift.model.Member;
import gift.model.Product;
import gift.paging.PagingService;
import gift.request.JoinRequest;
import gift.request.LoginMemberDto;
import gift.request.LoginRequest;
import gift.service.MemberService;
import gift.service.ProductService;
import gift.service.WishService;
import gift.paging.ArticlePage;
import gift.utils.ScriptUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PagingViewController {

//    private final ProductService productService;
//    private final MemberService memberService;
//    private final WishService wishService;
//    private final JwtService jwtService;
//    private final PagingService pagingService;
//
//    public PagingViewController(ProductService productService, MemberService memberService,
//        WishService wishService, JwtService jwtService, PagingService pagingService) {
//        this.productService = productService;
//        this.memberService = memberService;
//        this.wishService = wishService;
//        this.jwtService = jwtService;
//        this.pagingService = pagingService;
//    }
//
//    @GetMapping("/view/join")
//    public String joinForm(Model model) {
//        model.addAttribute("member", new Member("", ""));
//        return "joinForm";
//    }
//
//    @PostMapping("/view/join")
//    public String join(@ModelAttribute("member") @Valid JoinRequest joinRequest,
//        BindingResult bindingResult, HttpServletResponse response) {
//        if (bindingResult.hasErrors()) {
//            return "joinForm";
//        }
//
//        Member joinedMember = memberService.join(joinRequest.email(), joinRequest.password());
//        jwtService.addTokenInHeader(joinedMember, response);
//        return "redirect:/view/products";
//    }
//
//    @GetMapping("/view/login")
//    public String loginForm(Model model) {
//        model.addAttribute("member", new Member("", ""));
//        return "loginForm";
//    }
//
//    @PostMapping("/view/login")
//    public String login(@ModelAttribute("member") @Valid LoginRequest loginRequest,
//        BindingResult bindingResult, HttpServletResponse response) {
//        if (bindingResult.hasErrors()) {
//            return "loginForm";
//        }
//
//        Member loginedMember = memberService.login(loginRequest.email(), loginRequest.password());
//        jwtService.addTokenInCookie(loginedMember, response);
//        return "redirect:/view/products";
//    }
//
//    @GetMapping("/view/products")
//    public String getAllProducts(@RequestParam(defaultValue = "id") String sortOption,
//        @RequestParam(defaultValue = "1") int page, Model model) {
//        PageRequest pageRequest = pagingService.makeProductsPageRequest(page, sortOption);
//        Page<Product> productsInPage = productService.getPagedAllProducts(pageRequest);
//        ArticlePage articlePage = new ArticlePage(productsInPage, page, PRODUCTS_PER_PAGE,
//            SHOWING_PAGE_COUNT);
//
//        model.addAttribute("products", productsInPage.getContent());
//        model.addAttribute("pagingInfo", articlePage);
//        model.addAttribute("sortOption", sortOption);
//        return "pagingProducts";
//    }
//
//    @CheckRole("ROLE_USER")
//    @GetMapping("/view/wish")
//    public String getWishes(@RequestParam(defaultValue = "id") String sortOption,
//        @RequestParam(defaultValue = "1") int page, @LoginMember LoginMemberDto loginMemberDto,
//        Model model) {
//
//        PageRequest pageRequest = pagingService.makeWishPageRequest(page, sortOption);
//        Page<Product> wishListInPage = wishService.getPagedWishList(loginMemberDto.id(),
//            pageRequest);
//        ArticlePage articlePage = new ArticlePage(wishListInPage, page, WISH_PER_PAGE,
//            SHOWING_PAGE_COUNT);
//
//        model.addAttribute("products", wishListInPage.getContent());
//        model.addAttribute("pagingInfo", articlePage);
//        model.addAttribute("sortOption", sortOption);
//        return "wish";
//    }
//
//    @CheckRole("ROLE_USER")
//    @PostMapping("/view/wish/add")
//    public void addWish(@RequestParam("id") Long id, @LoginMember LoginMemberDto loginMemberDto
//        , HttpServletResponse response) throws IOException {
//        try {
//            wishService.addMyWish(loginMemberDto.id(), id);
//            ScriptUtils.alertAndMovePage(response, "정상적으로 추가되었습니다.", "/view/products");
//        } catch (WishException e) {
//            ScriptUtils.alertAndBackPage(response, e.getMessage());
//        }
//    }
//
//    @CheckRole("ROLE_USER")
//    @PostMapping("/view/wish/delete")
//    public void deleteWish(@RequestParam("id") Long id, @LoginMember LoginMemberDto loginMemberDto,
//        HttpServletResponse response) throws IOException {
//        try {
//            wishService.deleteMyWish(loginMemberDto.id(), id);
//            ScriptUtils.alertAndMovePage(response, "정상적으로 삭제되었습니다.", "/view/wish");
//        } catch (WishException | IOException e) {
//            ScriptUtils.alertAndBackPage(response, e.getMessage());
//        }
//    }


}
