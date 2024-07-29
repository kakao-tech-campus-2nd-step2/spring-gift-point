package gift.controller;

import gift.domain.AppUser;
import gift.dto.wish.AddWishRequest;
import gift.dto.wish.WishListResponse;
import gift.service.WishListService;
import gift.util.aspect.AdminController;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "WishList", description = "WishList Admin API")
public class WishListAdminController {
    private final WishListService wishListService;

    public WishListAdminController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @Operation(summary = "관리자 권한으로 유저 위시리스트 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Page<WishListResponse>> getWishListForAdmin(@LoginUser AppUser loginAppUser,
                                                                      @PathVariable("userId") Long userId,
                                                                      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                                      Pageable pageable) {
        Page<WishListResponse> responses = wishListService.getWishList(userId, pageable);
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "관리자 권한으로 유저 위시리스트 추가")
    @PostMapping("/{userId}")
    public ResponseEntity<String> addWishForAdmin(@LoginUser AppUser loginAppUser, @PathVariable("userId") Long userId,
                                                  @RequestBody AddWishRequest addWishRequest) {
        wishListService.addWish(userId, addWishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @Operation(summary = "관리자 권한으로 유저 위시리스트 상품 수량 수정")
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateWishQuantityForAdmin(@LoginUser AppUser loginAppUser,
                                                             @PathVariable("userId") Long userId,
                                                             @RequestParam Long wishId,
                                                             @RequestParam int quantity) {
        wishListService.updateWishQuantity(userId, wishId, quantity);
        return ResponseEntity.ok().body("ok");
    }


    @Operation(summary = "관리자 권한으로 유저 위시리스트 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteWishForAdmin(@LoginUser AppUser loginAppUser,
                                                     @PathVariable("userId") Long userId,
                                                     @RequestParam Long wishId) {
        wishListService.deleteWish(userId, wishId);
        return ResponseEntity.ok().body("ok");
    }
}
