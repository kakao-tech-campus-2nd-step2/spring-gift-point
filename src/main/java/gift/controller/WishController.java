package gift.controller;

import gift.dto.wish.WishCreateRequest;
import gift.dto.wish.WishResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시리스트 조회", description = "회원의 위시리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<WishResponse>> getWishlist(
        @RequestAttribute("memberId") Long memberId,
        @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<WishResponse> wishlist = wishService.getWishlistByMemberId(memberId, pageable);
        return ResponseEntity.ok(wishlist);
    }

    @Operation(summary = "위시리스트 추가", description = "회원의 위시리스트에 새로운 항목을 추가합니다.")
    @PostMapping
    public ResponseEntity<WishResponse> addWish(
        @Valid @RequestBody WishCreateRequest wishCreateRequest,
        @RequestAttribute("memberId") Long memberId
    ) {
        WishResponse createdWish = wishService.addWish(wishCreateRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWish);
    }

    @Operation(summary = "위시리스트 항목 삭제", description = "회원의 위시리스트에서 특정 항목을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(
        @PathVariable("id") Long id,
        @RequestAttribute("memberId") Long memberId
    ) {
        wishService.deleteWish(id, memberId);
        return ResponseEntity.noContent().build();
    }
}
