package gift.domain.wish;

import static org.springframework.data.domain.Sort.Direction.*;

import gift.domain.member.dto.LoginInfo;
import gift.domain.wish.dto.WishPageResponse;
import gift.domain.wish.dto.WishResponse;
import gift.global.resolver.Login;
import gift.global.response.ResponseMaker;
import gift.global.response.SimpleResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wish", description = "Wish API")
public class WishRestController {

    private final WishService wishService;

    public WishRestController(WishService wishService) {
        this.wishService = wishService;
    }


    /**
     * 장바구니에 상품 담기
     */
    @PostMapping("/products/{productId}")
    @Operation(summary = "위시 리스트 상품 추가")
    public ResponseEntity addWish(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        wishService.addWish(loginInfo.getId(), productId);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 상품 삭제
     */
    @DeleteMapping("/{wishId}")
    @Operation(summary = "장바구니 상품 삭제")
    public ResponseEntity deleteWish(
        @Parameter(description = "장바구니 상품(Wish) ID") @PathVariable("wishId") Long wishId,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        wishService.deleteWish(wishId);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 조회 - 페이징(매개변수별)
     */
    @GetMapping
    @Operation(summary = "장바구니 조회 - 페이징")
    public ResponseEntity<WishPageResponse> getProductsInWishByUserIdAndPageAndSort(
        @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "0") int page,
        @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "10") int size,
        @Parameter(description = "정렬 기준") @RequestParam(value = "sort", defaultValue = "createdDate_desc") String sort,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        Sort sortObj = getSortObject(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortObj);

        WishPageResponse wishPageResponse = wishService.getProductsInWish(loginInfo.getId(),
            pageRequest);
        return ResponseEntity.ok(wishPageResponse);
    }

    /**
     * 장바구니 상품 수량 변경
     */
    @PutMapping("/{wishId}")
    @Operation(summary = "장바구니 상품 수량 변경")
    public ResponseEntity<SimpleResultResponseDto> updateWishCount(
        @Parameter(description = "장바구니 상품(Wish) ID") @PathVariable("wishId") Long wishId,
        @Parameter(description = "변경할 상품 수량") @RequestParam("count") int count,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        wishService.updateWishCount(wishId, count);
        return ResponseEntity.ok().build();
    }

    private Sort getSortObject(String sort) {
        switch (sort) {
            case "price_asc":
                return Sort.by(ASC, "price");
            case "price_desc":
                return Sort.by(DESC, "price");
            case "createdDate_asc":
                return Sort.by(ASC, "createdDate");
            default:
                return Sort.by(DESC, "createdDate");
        }
    }
}
