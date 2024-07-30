package gift.product.presentation;

import gift.product.application.WishListService;
import gift.product.domain.AddWishListRequest;
import gift.product.domain.WishList;
import gift.util.CommonResponse;
import gift.util.annotation.JwtAuthenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WishListController", description = "위시리스트 관련 API")
@RestController
@RequestMapping("/api")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }


    @Operation(summary = "위시리스트 조회", description = "사용자의 위시리스트를 조회합니다.")
    @GetMapping("/wishes")
    @JwtAuthenticated
    public ResponseEntity<?> getWishList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort sorting = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        Page<WishList> products = wishListService.getProductsInWishList(userId, pageable);
        return ResponseEntity.ok(new CommonResponse<>(products, "위시리스트 조회 성공", true));
    }

    @JwtAuthenticated
    @Operation(summary = "위시리스트 생성", description = "새로운 위시리스트를 생성합니다.")
    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createWishList(@PathVariable Long userId) {
        wishListService.createWishList(userId);
        return ResponseEntity.ok(new CommonResponse<>(null, "위시리스트 생성 성공", true));
    }

    @JwtAuthenticated
    @Operation(summary = "위시리스트에 제품 추가", description = "위시리스트에 제품을 추가합니다.")
    @PostMapping("/{wishListId}/add/{productId}/{optionId}/{quentity}")
    public ResponseEntity<?> addProductToWishList(
            @PathVariable Long wishListId,
            @PathVariable Long productId,
            @PathVariable Long optionId,
            @PathVariable Long quentity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        wishListService.addProductToWishList(new AddWishListRequest(userId, wishListId, productId, optionId, quentity));
        return ResponseEntity.ok(new CommonResponse<>(null, "위시리스트에 제품 추가 성공", true));
    }

    @JwtAuthenticated
    @Operation(summary = "위시리스트에서 제품 삭제", description = "위시리스트에서 제품을 삭제합니다.")
    @DeleteMapping("/{userId}/delete/{productId}")
    public ResponseEntity<?> deleteProductFromWishList(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        wishListService.deleteProductFromWishList(userId, productId);
        return ResponseEntity.ok(new CommonResponse<>(null, "제품 삭제 성공", true));
    }
}
