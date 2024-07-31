package gift.Controller;

import gift.Model.response.WishResponse;
import gift.Service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/wishlist")
public class WishlistController {
    private final WishService wishService;
    private String email = "admin";

    public WishlistController(WishService wishService){
        this.wishService = wishService;
    }

    @GetMapping
    public String read(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 @RequestParam(value = "sort", defaultValue = "10") String sort,
                                 Model model){
        Page<WishResponse> wishlists = wishService.getPage(email, page, size, sort);
        model.addAttribute("wishlists", wishlists);
        return "wishlist";
    }

}
