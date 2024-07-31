package gift.wish.controller;

import gift.kakao.login.dto.KakaoUser;
import gift.product.domain.ProductRequest;
import gift.user.repository.UserRepository;
import gift.wish.domain.WishlistRequest;
import gift.product.service.ProductService;
import gift.user.service.UserService;
import gift.wish.domain.WishlistResponse;
import gift.wish.service.WishlistService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/wishlist")
public class WishlistViewController {
    private UserService userService;
    private ProductService productService;
    private WishlistService wishlistService;
    private UserRepository userRepository;

    public WishlistViewController(UserService userService, ProductService productService,
        WishlistService wishlistService, UserRepository userRepository) {
        this.userService = userService;
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.userRepository = userRepository;
    }

    @GetMapping("{id}")
    public String showWishlist(@PathVariable("id") Long userId, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WishlistResponse> wishlists = wishlistService.getWishlistResponseByUserId(userId, pageable);
        model.addAttribute("wishlists", wishlists);
        model.addAttribute("id", userId);
        String token = ((KakaoUser) userRepository.findById(userId).get()).getToken();
        model.addAttribute("token", token);
        System.out.println("token: " + token);
        return "wishlist";
    }

    @GetMapping("{id}/new")
    public String showAddProduct(@PathVariable("id") Long userId, Model model,
                                @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductRequest> productPages = productService.getAllProducts(pageable);
        model.addAttribute("products", productPages);
        model.addAttribute("userId", userId);
        return "add_wishlist";
    }
    @PostMapping("{id}/save")
    public String saveWishlist(@PathVariable("id") Long userId, @RequestBody List<WishlistRequest> wishlistRequests) {
        wishlistService.addWishes(wishlistRequests);
        return "redirect:/wishlist/" + userId;
    }
}
