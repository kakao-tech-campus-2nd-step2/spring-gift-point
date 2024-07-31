package gift.controller;

import static gift.util.Utils.DEFAULT_PAGE_SIZE;

import gift.domain.AppUser;
import gift.dto.common.CommonResponse;
import gift.dto.wish.AddWishRequest;
import gift.dto.wish.WishListResponse;
import gift.service.WishListService;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

    @Operation(summary = "로그인 유저 위시리스트 전체 조회", description = "위시리스트를 page로 반환",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            })
    @GetMapping
    public ResponseEntity<?> getWishListForUser(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "id", direction = Sort.Direction.DESC)
                                                Pageable pageable) {
        Page<WishListResponse> responses = wishListService.getWishList(loginAppUser.getId(), pageable);
        return ResponseEntity.ok(new CommonResponse<>(responses, "유저 위시리스트 전체 조회가 완료되었습니다.", true));
    }

    @Operation(summary = "위시리스트 상품 추가",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            })
    @PostMapping
    public ResponseEntity<?> addWish(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                     @RequestBody AddWishRequest addWishRequest) {
        wishListService.addWish(loginAppUser.getId(), addWishRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(null, "유저 위시리스트 상품 추가가 완료되었습니다.", true));
    }

    @Operation(summary = "위시리스트 상품 수량 수정",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            })
    @PatchMapping
    public ResponseEntity<?> updateWishQuantity(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                @RequestParam Long wishId,
                                                @RequestParam int quantity) {
        wishListService.updateWishQuantity(loginAppUser.getId(), wishId, quantity);
        return ResponseEntity.ok(new CommonResponse<>(null, "유저 위시리스트 상품 수량 수정이 완료되었습니다.", true));
    }

    @Operation(summary = "위시리스트 상품 삭제",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            })
    @DeleteMapping
    public ResponseEntity<?> deleteWish(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                        @RequestParam Long wishId) {
        wishListService.deleteWish(loginAppUser.getId(), wishId);
        return ResponseEntity.ok(new CommonResponse<>(null, "유저 위시리스트 상품 삭제가 완료되었습니다.", true));
    }
}
