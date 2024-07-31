package gift.controller;

import gift.Login;
import gift.dto.LoginMember;
import gift.dto.WishProduct;
import gift.dto.response.MessageResponse;
import gift.dto.response.WishProductResponse;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishes")
@Tag(name = "WishList", description = "위시 리스트와 관련된 API Controller")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    @Operation(summary = "위시 리스트 조회 api")
    public ResponseEntity<Page<WishProductResponse>> getWishList(@Login LoginMember member, Pageable pageable) {
        Page<WishProductResponse> wishes = wishListService.getWishList(member.getId(), pageable);
        return ResponseEntity.ok(wishes);
    }

    @PostMapping("/{productId}")
    @Operation(summary = "위시 상품 추가 api")
    public ResponseEntity<MessageResponse> addWishProduct(@Login LoginMember member, @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(wishListService.addWishProduct(new WishProduct(member.getId(), productId)));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "위시 리스트의 상품 삭제 api")
    public ResponseEntity<MessageResponse> deleteWishProduct(@Login LoginMember member, @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(wishListService.deleteWishProduct(new WishProduct(member.getId(), productId)));
    }
}
