package gift.controller;

import gift.domain.WishlistDTO;
import gift.service.ProductService;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "위시리스트", description = "위시리스트 관련 API")
@RestController
@RequestMapping("/api/wishes")
public class WishlistController {
    private final WishlistService wishlistService;
    private final ProductService productService;

    public WishlistController(WishlistService wishlistService, ProductService productService) {
        this.wishlistService = wishlistService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<WishlistDTO>> getAllWishlist(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<WishlistDTO> wishlistPage = wishlistService.getAllWishlist(token, page, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(wishlistPage.getNumber()));
        headers.add("X-Page-Size", String.valueOf(wishlistPage.getSize()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(wishlistPage);
    }

    @PostMapping
    public ResponseEntity<String> addWishlist(@RequestHeader("Authorization") String token, @RequestBody int productId) {
        wishlistService.addItem(token, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWishlist(@RequestHeader("Authorization") String token, @RequestBody int productId) {
        wishlistService.deleteItem(token, productId);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }

    @PutMapping
    public ResponseEntity<String> updateWishlist(@RequestHeader("Authorization") String token, @RequestBody int productId, @RequestBody int num) {
        wishlistService.changeNum(token, productId, num);
        return ResponseEntity.ok("상품 수량이 변경되었습니다.");
    }

    @GetMapping("/recommend")
    public ResponseEntity<Map<String, Integer>> getCommonCategory(@RequestHeader("Authorization") String token) {
        var body = wishlistService.getRecommendations(token);
        return ResponseEntity.ok().body(body);
    }
}
