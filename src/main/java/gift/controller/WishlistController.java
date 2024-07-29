package gift.controller;

import gift.dto.WishDto;
import gift.service.JwtUtil;
import gift.service.WishlistService;
import gift.vo.Wish;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishlistController {

    private final WishlistService service;
    private final JwtUtil jwtUtil;

    public WishlistController(WishlistService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/wishlist")
    @Operation(
            summary = "위시리스트 조회",
            description = "회원의 위시리스트를 페이징하여 조회하는 API입니다."
    )
    public ResponseEntity<Page<WishDto>> getWishProductList(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        Long memberId = jwtUtil.getMemberIdFromAuthorizationHeader(authorizationHeader);

        Page<Wish> allWishlistsPaged = service.getWishProductList(memberId, pageNumber-1, pageSize);

        return new ResponseEntity<>(allWishlistsPaged.map(WishDto::toWishDto), HttpStatus.OK);
    }

    @PostMapping("/wishlist/{productId}")
    @Operation(
            summary = "위시리스트에 상품 추가",
            description = "주어진 ID에 해당하는 특정 상품을 회원의 위시리스트에 추가하는 API입니다."
    )
    public ResponseEntity<Void> addToWishlist(@PathVariable("productId") Long productId, @RequestHeader("Authorization") String authorizationHeader) {
        Long memberId = jwtUtil.getMemberIdFromAuthorizationHeader(authorizationHeader);

        service.addWishProduct(memberId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/wishlist/{wishProductId}")
    @Operation(
            summary = "위시리스트에서 상품 삭제",
            description = "위시리스트에서 주어진 ID에 해당하는 특정 상품을 삭제하는 API입니다."
    )
    public ResponseEntity<Void> deleteToWishlist(@PathVariable("wishProductId") Long wishProductId, @RequestHeader("Authorization") String authorizationHeader) {
        service.deleteWishProduct(wishProductId);

        return ResponseEntity.ok().build();
    }

}
