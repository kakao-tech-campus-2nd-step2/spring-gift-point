package gift.controller;

import gift.dto.WishDTO;
import gift.service.WishService;
import gift.util.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    @Autowired
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시 상품 추가")
    @PostMapping
    public ResponseEntity<WishDTO> addWish(@LoginMember Long memberId, @RequestBody WishDTO wishDTO) {
        WishDTO savedWish = wishService.addWish(memberId, wishDTO.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWish);
    }

    @Operation(summary = "위시리스트 상품 삭제")
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@LoginMember Long memberId, @PathVariable Long wishId) {
        wishService.deleteWish(wishId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "위시리스트 상품 조회 (페이지네이션)")
    @GetMapping
    public ResponseEntity<Page<WishDTO>> getAllWishes(@LoginMember Long memberId, Pageable pageable) {
        Page<WishDTO> wishes = wishService.getWishesByMemberId(memberId, pageable);
        return ResponseEntity.ok(wishes);
    }
}
