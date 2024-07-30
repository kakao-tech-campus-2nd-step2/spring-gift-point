package gift.controller;

import gift.dto.ProductDTO;
import gift.model.User;
import gift.service.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * 위시리스트에 제품 추가.
     *
     * @param productId 제품 ID
     * @param session HTTP 세션
     * @return 응답 엔티티
     */
    @PostMapping("/{productId}")
    public ResponseEntity<Void> addWishlist(@PathVariable("productId") Long productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        wishlistService.addWishlist(user, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 사용자의 위시리스트 조회.
     *
     * @param session HTTP 세션
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 제품 목록
     */
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getWishlist(HttpSession session,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size) {
        User user = (User) session.getAttribute("user");
        Page<ProductDTO> products = wishlistService.getProductsFromWishlist(user, page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * 위시리스트에서 제품 삭제.
     *
     * @param productId 제품 ID
     * @param session HTTP 세션
     * @return 응답 엔티티
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable("productId") Long productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        wishlistService.deleteWishlist(user, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}