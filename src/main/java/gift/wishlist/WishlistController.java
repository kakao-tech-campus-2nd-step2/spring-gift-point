package gift.wishlist;

import gift.exception.InvalidProduct;
import gift.exception.UnAuthorizationException;
import gift.login.LoginMember;
import gift.logout.TokenValidator;
import gift.member.Member;
import gift.product.Product;
import gift.product.ProductService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;
    private final TokenValidator tokenValidator;

    public WishlistController(WishlistService wishlistService, ProductService productService, TokenValidator tokenValidator) {
        this.wishlistService = wishlistService;
        this.productService = productService;
        this.tokenValidator = tokenValidator;
    }

    @PostMapping
    public WishResponseDto create(@RequestBody WishRequestDto request, @LoginMember Member member)
        throws UnAuthorizationException {
        return wishlistService.addWishlist(request, member);
    }

    @GetMapping
    public Page<WishPageDto> getWishlist(@LoginMember Member member, Pageable pageable) {
        return wishlistService.checkWishlist(member, pageable);
    }

    @DeleteMapping("/{wishId}")
    public void deleteWish(@PathVariable Long wishId, @LoginMember Member member)
        throws UnAuthorizationException {
        wishlistService.deleteWishlist(wishId, member.getId());
    }

}
