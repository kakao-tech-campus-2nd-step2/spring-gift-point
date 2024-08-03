package gift.controller;

import gift.domain.AppUser;
import gift.dto.common.CommonResponse;
import gift.dto.wish.AddWishRequest;
import gift.dto.wish.WishListResponse;
import gift.service.WishListService;
import gift.util.aspect.AdminController;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "관리자 권한으로 유저 위시리스트 조회",
            security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/{userId}")
    public ResponseEntity<?> getWishListForAdmin(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                 @PathVariable("userId") Long userId,
                                                 @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        Page<WishListResponse> responses = wishListService.getWishList(userId, pageable);
        return ResponseEntity.ok(new CommonResponse<>(responses, "관리자 권한으로 유저 위시리스트 조회가 완료되었습니다.", true));
    }

    @Operation(summary = "관리자 권한으로 유저 위시리스트 추가",
            security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/{userId}")
    public ResponseEntity<?> addWishForAdmin(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                             @PathVariable("userId") Long userId,
                                             @RequestBody AddWishRequest addWishRequest) {
        wishListService.addWish(userId, addWishRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(null, "관리자 권한으로 유저 위시리스트 추가가 완료되었습니다.", true));
    }

    @Operation(summary = "관리자 권한으로 유저 위시리스트 상품 수량 수정",
            security = @SecurityRequirement(name = "JWT"))
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateWishQuantityForAdmin(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                        @PathVariable("userId") Long userId,
                                                        @RequestParam Long wishId,
                                                        @RequestParam int quantity) {
        wishListService.updateWishQuantity(userId, wishId, quantity);
        return ResponseEntity.ok(new CommonResponse<>(null, "관리자 권한으로 유저 위시리스트 상품 수량 수정이 완료되었습니다.", true));
    }


    @Operation(summary = "관리자 권한으로 유저 위시리스트 삭제",
            security = @SecurityRequirement(name = "JWT"))
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteWishForAdmin(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                @PathVariable("userId") Long userId,
                                                @RequestParam Long wishId) {
        wishListService.deleteWish(userId, wishId);
        return ResponseEntity.ok(new CommonResponse<>(null, "관리자 권한으로 유저 위시리스트 삭제가 완료되었습니다.", true));
    }
}
