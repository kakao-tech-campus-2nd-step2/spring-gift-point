package gift.wishlist.controller;

import static gift.util.Utils.DEFAULT_PAGE_SIZE;

import gift.resolver.LoginUser;
import gift.user.model.dto.AppUser;
import gift.wishlist.model.dto.AddWishRequest;
import gift.wishlist.model.dto.WishListResponse;
import gift.wishlist.service.WishListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<Page<WishListResponse>> getWishListForUser(@LoginUser AppUser loginAppUser,
                                                                     @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "id", direction = Sort.Direction.DESC)
                                                                     Pageable pageable) {
        Page<WishListResponse> responses = wishListService.getWishList(loginAppUser.getId(), pageable);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<String> addWish(@LoginUser AppUser loginAppUser, @RequestBody AddWishRequest addWishRequest) {
        wishListService.addWish(loginAppUser.getId(), addWishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PatchMapping
    public ResponseEntity<String> updateWishQuantity(@LoginUser AppUser loginAppUser, @RequestParam Long wishId,
                                                     @RequestParam int quantity) {
        wishListService.updateWishQuantity(loginAppUser.getId(), wishId, quantity);
        return ResponseEntity.ok().body("ok");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWish(@LoginUser AppUser loginAppUser, @RequestParam Long wishId) {
        wishListService.deleteWish(loginAppUser.getId(), wishId);
        return ResponseEntity.ok().body("ok");
    }
}
