package gift.Controller;

import gift.Model.DTO.ProductDTO;
import gift.Model.DTO.WishDTO;
import gift.Service.WishService;
import gift.Token.JwtTokenProvider;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/wishlist")
public class WishlistController {
    private final WishService wishService;
    private String email = "admin";

    public WishlistController(WishService wishService){
        this.wishService = wishService;
    }

    @GetMapping
    public String getAllWishlist(@RequestParam(value = "page", defaultValue = "0") int page, Model model){
        Page<String> wishlists = wishService.getPage(email, page);
        model.addAttribute("wishlists", wishlists);
        return "wishlist";
    }

}
