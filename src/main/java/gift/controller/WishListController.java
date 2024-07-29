package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.*;
import gift.model.User;
import gift.service.ProductService;
import gift.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wishes")
public class WishListController {
    private final WishService wishService;
    private final ProductService productService;

    public WishListController(WishService wishService, ProductService productService) {
        this.wishService = wishService;
        this.productService = productService;
    }

    @GetMapping
    public String getWishlist(@LoginUser User user, Model model, @PageableDefault(size = 3) Pageable pageable) {
        WishPageResponseDTO wishOptions = wishService.getWishlist(user.getId(), pageable);
        model.addAttribute("wishOptions", wishOptions);
        return "wishlist";
    }

    @GetMapping("/addWishOption")
    public String addWishOptionPage(@LoginUser User user, Model model, @PageableDefault(size = 3) Pageable pageable) {
        ProductsPageResponseDTO products = productService.getAllProducts(pageable);
        model.addAttribute("products", products);

        return "addWishProduct"; // addWishProduct.html로 이동
    }

    @PostMapping("/addWishOption")
    public ResponseEntity<String> addWishOption(@LoginUser User user, @RequestBody WishRequestDTO wishRequestDTO, Model model) {
        wishService.addWishOption(user.getId(), wishRequestDTO);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @DeleteMapping
    public String deleteWishProduct(@LoginUser User user, @RequestBody WishRequestDTO wishRequestDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        wishService.deleteWishOption(user.getId(), wishRequestDTO.optionId());

        WishPageResponseDTO wishOptions = wishService.getWishlist(user.getId(), pageable);
        model.addAttribute("wishOptions", wishOptions);

        return "wishlist";
    }
}
