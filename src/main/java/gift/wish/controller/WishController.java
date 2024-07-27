package gift.wish.controller;

import gift.common.auth.LoginMember;
import gift.member.model.Member;
import gift.wish.model.WishDTO;
import gift.wish.model.WishRequest;
import gift.wish.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wish")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }


    // 1. 사용자 위시리스트에 상품 추가
    @PostMapping
    public void createWish(@LoginMember Member member, @RequestBody WishRequest wishRequest) {
        wishService.createWish(member, wishRequest.getProductId());
    }

    // 2. 사용자 위시리스트 상품 전체 조회
    @GetMapping
    public List<WishDTO> getWishlist(@LoginMember Member member) {
        return wishService.getWishlistByMemberId(member);
    }

    // 3. 사용자의 위시리스트 삭제
    @DeleteMapping("/{wishId}")
    public void deleteWish(@LoginMember Member member, @PathVariable Long wishId) {
        wishService.deleteWish(wishId);
    }

    // 4. 전체 위시리스트 조회
    @GetMapping("/all")
    public Page<WishDTO> getWishlistByPagination(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam String sortBy,
                                      @RequestParam String direction) {
        return wishService.getWishlistByPage(page, size, sortBy, direction);
    }
}
