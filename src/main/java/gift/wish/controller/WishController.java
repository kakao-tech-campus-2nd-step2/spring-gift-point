package gift.wish.controller;

import gift.common.auth.LoginMember;
import gift.common.util.CommonResponse;
import gift.member.model.Member;
import gift.wish.model.WishDTO;
import gift.wish.model.WishRequest;
import gift.wish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wish")
@Tag(name = "Wish", description = "WishList API")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }


    // 1. 사용자 위시리스트에 상품 추가
    @Operation(summary = "위시리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
    @PostMapping
    public ResponseEntity<?> createWish(@LoginMember Member member, @RequestBody WishRequest wishRequest) {
        wishService.createWish(member, wishRequest.getProductId());

        return ResponseEntity.ok(new CommonResponse<>(null, "위시리스트에 상품이 추가되었습니다.", true));
    }

    // 2. 사용자 위시리스트 상품 전체 조회
    @Operation(summary = "위시 리스트 상품 조회 (페이지네이션 적용)", description = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<?> getWishlist(@LoginMember Member member) {

        List <WishDTO> wishList = wishService.getWishlistByMemberId(member);
        return ResponseEntity.ok(new CommonResponse<>(wishList, "위시리스트 조회 성공", true));
    }

    // 3. 사용자의 위시리스트 삭제
    @Operation(summary = "위시리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
    @DeleteMapping("/{wishId}")
    public ResponseEntity<?> deleteWish(@LoginMember Member member, @PathVariable Long wishId) {
        wishService.deleteWish(wishId);

        return ResponseEntity.ok(new CommonResponse<>(null, "위시리스트 상품이 삭제되었습니다.", true));
    }

    // 4. 전체 위시리스트 조회
    @GetMapping("/all")
    public ResponseEntity<?> getWishlistByPagination(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam String sortBy,
                                      @RequestParam String direction) {

        Page<WishDTO> wishList = wishService.getWishlistByPage(page, size, sortBy, direction);

        return ResponseEntity.ok(new CommonResponse<>(wishList, "전체 위시리스트 조회 성공", true));
    }
}
