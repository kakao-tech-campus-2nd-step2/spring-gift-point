package gift.controller;

import gift.dto.PageRequestDTO;
import gift.dto.wishDTOs.CustomWishPageDTO;
import gift.model.entity.Member;
import gift.model.valueObject.BearerToken;
import gift.service.KakaoAuthService;
import gift.service.WishService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.Map;

@RestController
@RequestMapping("api/wishes")
public class WishController {
    private final WishService wishService;
    private final KakaoAuthService kakaoAuthService;

    public WishController(WishService wishService, KakaoAuthService kakaoAuthService) {
        this.wishService = wishService;
        this.kakaoAuthService = kakaoAuthService;
    }

    //멤버 id로 해당 멤버의 위시리스트 가져옴
    @GetMapping
    public CustomWishPageDTO getWishlistController(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) throws AuthenticationException {
        BearerToken token = (BearerToken) request.getAttribute("bearerToken");
        long memberId = kakaoAuthService.getDBMemberByToken(token.getToken()).getId();
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, "id", "asc");
        CustomWishPageDTO wishlist = wishService.getWishlist(memberId, pageRequestDTO);

        return wishlist;
    }

    //위시리스트 상품 추가
    @PostMapping
    public void postWishlist(@RequestBody Map<String, Long> requestBody, HttpServletRequest request) throws AuthenticationException {
        BearerToken token = (BearerToken) request.getAttribute("bearerToken");
        Member member = kakaoAuthService.getDBMemberByToken(token.getToken());
        Long productId = requestBody.get("productId");
        wishService.postWishlist(productId, member);
    }

    //위시리스크 상품 wishlist id 받아와 삭제
    @DeleteMapping("/{whishId}")
    public void deleteProductController(@PathVariable Long whishId) {
        wishService.deleteProduct(whishId);
    }
}