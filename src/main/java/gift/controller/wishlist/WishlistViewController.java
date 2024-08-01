package gift.controller.wishlist;

import gift.DTO.product.ProductResponse;
import gift.domain.Product;
import gift.service.TokenService;
import gift.service.WishlistService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/my/wishlist")
public class WishlistViewController {

    private final WishlistService wishlistService;
    private final TokenService tokenService;

    @Autowired
    public WishlistViewController(
        WishlistService wishlistService,
        TokenService tokenService
    ) {
        this.wishlistService = wishlistService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public String showWishlist(
        Model model,
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "2") Integer size
    ) {
        if (authHeader == null) {
            return "login";
        }

        if (!authHeader.startsWith("Bearer ")) {
            model.addAttribute("error", "Invalid -token");
            return "error";
        }

        String token = authHeader.substring(7); // "Bearer " 제거
        if (!tokenService.validateToken(token)) {
            model.addAttribute("error", "Fail to validate token");
            return "error";
        }

        String email = tokenService.extractEmailFromToken(token);
        List<ProductResponse> wishlist = wishlistService.getWishlistByEmail(email, page, size);
        model.addAttribute("wishlist", wishlist);
        return "wishlist";
    }
}