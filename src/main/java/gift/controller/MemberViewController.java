package gift.controller;

import gift.dto.ProductResponse;
import gift.service.MemberService;
import gift.utils.PageNumberListGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/members")
public class MemberViewController {

    private final MemberService memberService;

    public MemberViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    public String registerMemberForm() {
        return "memberRegister";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/wishlist")
    public String getWishlist(Model model, @PageableDefault(size = 5) Pageable pageable,
        HttpServletRequest httpServletRequest) {
        String email = (String) httpServletRequest.getAttribute("email");
        Page<ProductResponse> wishlist = memberService.getAllWishlist(email, pageable);

        model.addAttribute("pageNumbers", PageNumberListGenerator.generatePageNumberList(wishlist));
        model.addAttribute("wishlist", wishlist);

        return "wishlist";
    }
}
