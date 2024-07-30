package gift.controller;

import gift.model.wishList.WishListResponse;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wish List Api")
@RestController
@RequestMapping("/api/wishes")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @Operation(summary = "위시 리스트 조회")
    @GetMapping
    public Page<WishListResponse> getWishList(@RequestAttribute("userId") Long userId,
        @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable) {
        return wishListService.getWishList(userId, pageable);
    }

    @Operation(summary = "위시 상품 추가")
    @PostMapping("/{id}")
    public ResponseEntity<Long> addToWishList(
        @Parameter(description = "추가할 상품 id") @PathVariable("id") Long itemId,
        @RequestAttribute("userId") Long userId) {
        Long wishId = wishListService.addToWishList(userId, itemId);
        return ResponseEntity.ok(wishId);
    }

    @Operation(summary = "위시 상품 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteFromWishList(
        @Parameter(description = "삭제할 상품 id") @PathVariable("id") Long id,
        @RequestAttribute("userId") Long userId) {
        wishListService.deleteFromWishList(id);
        return ResponseEntity.ok(id);
    }
}