package gift.controller;

import static gift.util.Utils.DEFAULT_PAGE_SIZE;

import gift.domain.AppUser;
import gift.dto.wish.AddWishRequest;
import gift.dto.wish.WishListResponse;
import gift.service.WishListService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "WishList", description = "WishList User API")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @Operation(summary = "로그인 유저 위시리스트 전체 조회", description = "위시리스트를 page로 반환")
    @GetMapping
    public ResponseEntity<Page<WishListResponse>> getWishListForUser(@LoginUser AppUser loginAppUser,
                                                                     @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "id", direction = Sort.Direction.DESC)
                                                                     Pageable pageable) {
        Page<WishListResponse> responses = wishListService.getWishList(loginAppUser.getId(), pageable);
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "위시리스트 상품 추가")
    @PostMapping
    public ResponseEntity<String> addWish(@LoginUser AppUser loginAppUser, @RequestBody AddWishRequest addWishRequest) {
        wishListService.addWish(loginAppUser.getId(), addWishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @Operation(summary = "위시리스트 상품 수량 수정")
    @PatchMapping
    public ResponseEntity<String> updateWishQuantity(@LoginUser AppUser loginAppUser, @RequestParam Long wishId,
                                                     @RequestParam int quantity) {
        wishListService.updateWishQuantity(loginAppUser.getId(), wishId, quantity);
        return ResponseEntity.ok().body("ok");
    }

    @Operation(summary = "위시리스트 상품 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteWish(@LoginUser AppUser loginAppUser, @RequestParam Long wishId) {
        wishListService.deleteWish(loginAppUser.getId(), wishId);
        return ResponseEntity.ok().body("ok");
    }
}
