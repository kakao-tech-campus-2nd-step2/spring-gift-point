package gift.wishlist.controller;

import gift.aspect.AdminController;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminController
@RequestMapping("/api/admin/wishes")
public class WishListAdminController {
    private final WishListService wishListService;

    public WishListAdminController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<WishListResponse>> getWishListForAdmin(@LoginUser AppUser loginAppUser,
                                                                      @PathVariable("userId") Long userId,
                                                                      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                                      Pageable pageable) {
        Page<WishListResponse> responses = wishListService.getWishList(userId, pageable);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> addWishForAdmin(@LoginUser AppUser loginAppUser, @PathVariable("userId") Long userId,
                                                  @RequestBody AddWishRequest addWishRequest) {
        wishListService.addWish(userId, addWishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateWishQuantityForAdmin(@LoginUser AppUser loginAppUser,
                                                             @PathVariable("userId") Long userId,
                                                             @RequestParam Long wishId,
                                                             @RequestParam int quantity) {
        wishListService.updateWishQuantity(userId, wishId, quantity);
        return ResponseEntity.ok().body("ok");
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteWishForAdmin(@LoginUser AppUser loginAppUser,
                                                     @PathVariable("userId") Long userId,
                                                     @RequestParam Long wishId) {
        wishListService.deleteWish(userId, wishId);
        return ResponseEntity.ok().body("ok");
    }
}
