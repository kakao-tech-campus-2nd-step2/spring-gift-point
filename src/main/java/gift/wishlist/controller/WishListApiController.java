package gift.wishlist.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import gift.global.annotation.UserId;
import gift.global.dto.PageInfoDto;
import gift.wishlist.dto.WishListResponseDto;
import gift.wishlist.service.WishListService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 개인의 wish list db를 조작해서 결과를 가져오는 api 컨트롤러

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class WishListApiController {

    private final WishListService wishListService;

    @Autowired
    public WishListApiController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    // 전체 목록에서 제품 추가 시
    @PostMapping("/wishes/{productId}")
    public ResponseEntity<Void> addWishProduct(@PathVariable(name = "productId") Long productId,
        @UserId Long userId) {
        wishListService.insertWishProduct(productId, userId);
        return status(HttpStatus.CREATED).build();
    }

    // 나의 위시 페이지 가져오기
    @GetMapping("/wishes")
    public ResponseEntity<List<WishListResponseDto>> getWishProducts(@UserId Long userId,
        @ModelAttribute PageInfoDto pageInfoDto) {
        return ok(wishListService.readWishProducts(userId, pageInfoDto));
    }

    // 삭제 버튼 눌러서 삭제
    @DeleteMapping("/wishes/{wishId}")
    public ResponseEntity<Void> deleteWishProduct(
        @PathVariable(name = "wishId") Long wishListId,
        @UserId Long userId) {
        wishListService.deleteWishProductWithVerification(wishListId, userId);
        return status(HttpStatus.NO_CONTENT).build();
    }
}
