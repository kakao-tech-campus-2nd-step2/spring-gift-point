package gift.controller;

import gift.dto.WishDto;
import gift.auth.JwtUtil;
import gift.service.WishlistService;
import gift.vo.Wish;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "위시리스트 관리", description = "위시리스트 조회, 추가, 삭제와 관련된 API들을 제공합니다.")
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
    @Parameters({
            @Parameter(name = "authorizationHeader", description = "회원 인증을 위한 Authorization 헤더", required = true),
            @Parameter(name = "pageNumber", description = "페이지 번호", example = "1"),
            @Parameter(name = "pageSize", description = "페이지 사이즈", example = "5")
    })
    public ResponseEntity<Page<WishDto>> getWishProductList(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        Long memberId = jwtUtil.getMemberIdFromAuthorizationHeader(authorizationHeader);

        Page<Wish> allWishlistsPaged = service.getWishProductList(memberId, pageNumber-1, pageSize);

        return ResponseEntity.ok().body(allWishlistsPaged.map(WishDto::toWishDto));
    }

    @PostMapping("/wishlist/{productId}")
    @Operation(
            summary = "위시리스트에 상품 추가",
            description = "주어진 ID에 해당하는 특정 상품을 회원의 위시리스트에 추가하는 API입니다."
    )
    @Parameters({
            @Parameter(name = "productId", description = "위시리스트에 추가할 상품의 ID", required = true),
            @Parameter(name = "authorizationHeader", description = "회원 인증을 위한 Authorization 헤더", required = true)
    })
    public ResponseEntity<Void> addToWishlist(@PathVariable("productId") Long productId, @RequestHeader("Authorization") String authorizationHeader) {
        Long memberId = jwtUtil.getMemberIdFromAuthorizationHeader(authorizationHeader);

        service.addWishProduct(memberId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/wishlist/{wishProductId}")
    @Operation(
            summary = "위시리스트에서 상품 삭제",
            description = "위시리스트에서 주어진 ID에 해당하는 특정 상품을 삭제하는 API입니다."
    )
    @Parameters({
            @Parameter(name = "wishProductId", description = "위시리스트에서 삭제할 상품의 ID", required = true),
            @Parameter(name = "authorizationHeader", description = "회원 인증을 위한 Authorization 헤더", required = true)
    })
    public ResponseEntity<Void> deleteToWishlist(@PathVariable("wishProductId") Long wishProductId, @RequestHeader("Authorization") String authorizationHeader) {
        service.deleteWishProduct(wishProductId);

        return ResponseEntity.ok().build();
    }

}
