package gift.controller;

import gift.dto.PaginationInfo;
import gift.dto.ProductDto;
import gift.dto.response.ProductResponse;
import gift.dto.response.WishlistResponse;

import gift.service.WishlistService;
import gift.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wishes")
@Tag(name = "[협업] WISHLIST API", description = "[협업]위시리스트 컨트롤러")
public class WishlistNewController {

    private final WishlistService wishlistService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WishlistNewController(WishlistService wishlistService,JwtTokenProvider jwtTokenProvider) {
        this.wishlistService = wishlistService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "위시리스트 목록을 보여줍니다.")
    public Page<WishlistResponse> getWishes(@RequestAttribute("userId") Long userId,
                                                           Pageable pageable){
        return wishlistService.getWishlistByMemberId(userId, pageable);
    }

    @PostMapping("/{productId}")
    @Operation(summary = "위시리스트 아이템 추가", description = "위시리스트에 특정 아이템을 추가합니다.")
    public ResponseEntity<Void> addWishlistItem(@RequestParam("email") String email, @PathVariable Long productId) {
        wishlistService.addWishlistItem(email, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트 아이템 삭제", description = "위시리스트에서 특정 아이템을 삭제합니다.")
    public ResponseEntity<Void> deleteWishlistItem(@RequestHeader("Authorization") String token, @PathVariable Long wishId) {
        String email = jwtTokenProvider.getEmail(token.substring(7));
        wishlistService.deleteWishlistItemById(wishId);
        return ResponseEntity.ok().build();
    }



}
