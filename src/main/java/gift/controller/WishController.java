package gift.controller;

import gift.dto.WishDTO;
import gift.model.Member;
import gift.service.WishService;
import gift.util.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    @Autowired
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "인증된 사용자의 모든 위시리스트 가져오기")
    @GetMapping
    public ResponseEntity<Page<WishDTO>> getAllWishes(@LoginMember Member member, Pageable pageable) {
        Page<WishDTO> wishes = wishService.getWishesByMemberId(member.getId(), pageable);
        return ResponseEntity.ok(wishes);
    }

    @Operation(summary = "인증된 사용자의 새 위시리스트 항목 추가")
    @PostMapping
    public ResponseEntity<WishDTO> addWish(@LoginMember Member member, @RequestBody Long productId) {
        WishDTO savedWish = wishService.addWish(member, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWish);
    }

    @Operation(summary = "인증된 사용자의 위시리스트 항목 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWish(@LoginMember Member member, @PathVariable Long productId) {
        wishService.deleteWish(member.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}
