package gift.wish.controller;

import gift.annotation.LoginMember;
import gift.member.model.Member;
import gift.model.HttpResult;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishResponse;
import gift.wish.model.Wish;
import gift.wish.service.WishService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<WishResponse> getWishes(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sort", defaultValue = "createdDate,DESC") String sort,
        @LoginMember Member member) {
        String[] sortParams = sort.split(",");
        String property = sortParams[0];
        Direction direction = sortParams.length > 1 ? Direction.valueOf(sortParams[1]) : Direction.ASC;
        Page<Wish> wishListPage = wishService.getWishListPage(member, page, size, property, direction);
        return ResponseEntity.ok(
            new WishResponse(HttpResult.OK, "장바구니 조회 성공", HttpStatus.OK, wishListPage));
    }

    @PostMapping
    public ResponseEntity<WishResponse> addWish(@RequestBody WishRequest wishRequest,
        @LoginMember Member member) {
        Wish savedWish = wishService.addWish(wishRequest.getProductId(), member);
        return ResponseEntity.ok(
            new WishResponse(HttpResult.OK, "장바구니 추가 성공", HttpStatus.OK, savedWish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WishResponse> removeWish(@PathVariable Long id,
        @LoginMember Member member) {
        wishService.removeWish(id, member);
        return ResponseEntity.ok(
            new WishResponse(HttpResult.OK, "장바구니 제거 성공", HttpStatus.OK, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishResponse> getWishesById(@PathVariable Long productId,
        @LoginMember Member member) {
        List<Wish> wishes = wishService.getWishesByMember(member);
        return ResponseEntity.ok(
            new WishResponse(HttpResult.OK, "장바구니 수정 성공", HttpStatus.OK, wishes));
    }
}
