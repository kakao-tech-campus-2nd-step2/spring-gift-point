package gift.wishlist.controller;

import static gift.global.dto.ApiResponseDto.SUCCESS;

import gift.global.annotation.UserId;
import gift.global.dto.ApiResponseDto;
import gift.global.dto.PageInfoDto;
import gift.wishlist.dto.WishListResponseDto;
import gift.wishlist.service.WishListService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 개인의 wish list db를 조작해서 결과를 가져오는 api 컨트롤러

@RestController
@RequestMapping("/api/users/")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class WishListApiController {

    private final WishListService wishListService;

    @Autowired
    public WishListApiController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    // 전체 목록에서 제품 추가 시
    @PostMapping("/wishlist")
    public ApiResponseDto<Void> addWishProduct(@RequestParam(name = "product-id") Long productId,
        @UserId Long userId) {
        wishListService.insertWishProduct(productId, userId);
        return SUCCESS();
    }

    // 나의 위시 페이지 가져오기
    @GetMapping("/wishlist")
    public ApiResponseDto<List<WishListResponseDto>> getWishProducts(@UserId Long userId,
        @ModelAttribute PageInfoDto pageInfoDto) {
        return SUCCESS(wishListService.readWishProducts(userId, pageInfoDto));
    }

    // 삭제 버튼 눌러서 삭제
    @DeleteMapping("/wishlist/{wishlist-id}")
    public ApiResponseDto<Void> deleteWishProduct(
        @PathVariable(name = "wishlist-id") Long wishListId,
        @UserId Long userId) {
        wishListService.deleteWishProduct(wishListId, userId);
        return SUCCESS();
    }
}
