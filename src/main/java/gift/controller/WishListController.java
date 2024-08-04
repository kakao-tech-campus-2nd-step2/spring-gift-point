package gift.controller;

import gift.dto.ProductDTO;
import gift.dto.WishListDTO;
import gift.service.WishListService;
import gift.service.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "위시리스트 API")
@RestController
@RequestMapping("/api/wish")
public class WishListController {
    private final WishListService wishListService;
    private final JwtUtil jwtUtil;

    @Autowired
    public WishListController(WishListService wishListService, JwtUtil jwtUtil) {
        this.wishListService = wishListService;
        this.jwtUtil = jwtUtil;
    }

    // 토큰에서 userId 추출
    private Long getUserId(String token) {
        return jwtUtil.extractUserId(token);

    }

    // 전체 위시리스트 조회
    @Operation(summary = "전체 위시리스트 조회", description = "위시리스트 전체를 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<WishListDTO>> getWishList(
        @RequestHeader("Authorization") String token,

        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
        @RequestParam(defaultValue = "0") int page,

        @Min(value = 1, message = "한 페이지당 1개 이상의 항목이 포함되어야 합니다.")
        @RequestParam(defaultValue = "10") int size) {
        String parsedToken = token.replace("Bearer ", "");
        Long userId = getUserId(parsedToken);
        Pageable pageable = PageRequest.of(page, size);
        Page<WishListDTO> wishList = wishListService.readWishList(userId, pageable);
        return ResponseEntity.ok(wishList);
    }

    // 위시리스트 추가
    @Operation(summary = "위시리스트 생성", description = "새 위시리스트를 생성합니다.")
    @PostMapping
    public ResponseEntity<WishListDTO> addWishList(@RequestHeader("Authorization") String token,
       @RequestBody ProductDTO product) throws Exception {
        System.out.println("Received ID: " + product.getId());
        String parsedToken = token.replace("Bearer ", "");
        Long userId = getUserId(parsedToken);
        WishListDTO response = wishListService.addProductToWishList(userId, product);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 특정 위시리스트를 삭제 (id = wishList Id)
    @Operation(summary = "특정 위시리스트 삭제", description = "특정 위시리스트를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeWishList(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        String parsedToken = token.replace("Bearer ", "");
        Long userId = getUserId(parsedToken);
        wishListService.removeWishList(userId);
        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.NO_CONTENT);
    }

//    // 위시리스트 특정 상품 삭제
//    @Operation(summary = "특정 상품을 가진 위시리스트 삭제", description = "특정 사용자의 특정 위시리스트 품목을 삭제합니다.")
//    @DeleteMapping("/delete/{productId}")
//    public ResponseEntity<String> removeProductFromWishList(@RequestHeader("Authorization") String token,
//        @PathVariable Long productId) {
//        String parsedToken = token.replace("Bearer ", "");
//        Long userId = getWishId(parsedToken);
//        wishListService.removeProductFromWishList(userId, productId);
//        return new ResponseEntity<>("Product removed from wishlist", HttpStatus.OK);
//    }

    // 전체 위시리스트 삭제
    @Operation(summary = "위시리스트 전체 삭제", description = "특정 사용자의 전체 위시리스트를 모두 삭제합니다.")
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> removeAllWishList(@RequestHeader("Authorization") String token) {
        String parsedToken = token.replace("Bearer ", "");
        Long userId = getUserId(parsedToken);
        wishListService.removeUserWishList(userId);
        return new ResponseEntity<>("Wishlist deleted", HttpStatus.OK);
    }
}
