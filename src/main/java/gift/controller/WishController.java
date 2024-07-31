package gift.controller;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishListDto;
import gift.security.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.service.WishService;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "특정 회원의 위시리스트 조회")
    @GetMapping
    public ResponseEntity<List<WishListDto>> getWishList(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken, Pageable pageable) {
        Page<WishListDto> wishPage = wishService.getWishPage(accessToken, pageable);
        return ResponseEntity.ok(wishPage.getContent());
    }

    @Operation(summary = "위시리스트에 상품 추가")
    @PostMapping("/{product_id}")
    public ResponseEntity<Void> addToWishList(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken, @PathVariable Long product_id) {
        wishService.addWish(accessToken, product_id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "위시리스트에서 삭제")
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> removeFromWishList(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken, @PathVariable Long product_id) {
        wishService.deleteWish(accessToken, product_id);
        return ResponseEntity.noContent().build();
    }
}

