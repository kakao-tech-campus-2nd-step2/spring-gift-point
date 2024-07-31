package gift.wishlist;

import gift.product.dto.ProductResponseDTO;
import gift.wishlist.dto.ProductIdRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wishlist", description = "Wishlist API")
@RestController
@RequestMapping("/wishes")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "사용자의 토큰을 통해 위시리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "400", description = "입력 양식이 잘못된 경우")
    @ApiResponse(responseCode = "403", description = "인증 실패")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public Page<ProductResponseDTO> getAllWishlists(
        @RequestHeader("Authorization") String token,
        @ParameterObject Pageable pageable
    ) {
        return wishlistService.getAllWishlists(token, pageable);
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "사용자의 위시리스트에 상품을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "정상")
    @ApiResponse(responseCode = "400", description = "상품이 존재하지 않는 경우")
    @ApiResponse(responseCode = "400", description = "요청 양식이 잘못된 경우")
    @ApiResponse(responseCode = "403", description = "잘못된 유저 토큰")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public void addWishlist(
        @RequestHeader("Authorization") String token,
        @RequestBody ProductIdRequestDTO productIdRequestDTO
    ) {
        wishlistService.addWishlist(token, productIdRequestDTO);
    }

    @DeleteMapping("/{product_id}")
    @Operation(summary = "위시리스트 삭제", description = "사용자의 위시리스트에서 해당 상품을 제거합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "상품이 존재하지 않는 경우")
    @ApiResponse(responseCode = "400", description = "해당 상품의 위시리스트가 존재하지 않는 경우")
    @ApiResponse(responseCode = "400", description = "요청 양식이 잘못된 경우")
    @ApiResponse(responseCode = "403", description = "인증 실패")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public void deleteWishlist(
        @RequestHeader("Authorization") String token,
        @PathVariable(name = "product_id") long productId
    ) {
        wishlistService.deleteWishlist(token, productId);
    }
}
