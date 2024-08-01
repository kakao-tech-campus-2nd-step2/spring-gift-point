package gift.web.controller;

import gift.service.wish.WishService;
import gift.web.dto.MemberDto;
import gift.web.dto.WishDto;
import gift.web.jwt.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<WishDto>> getWishes(@AuthUser MemberDto memberDto, Pageable pageable) {
        // todo : 관리자 권한 검증 로직 구현
        return new ResponseEntity<>(wishService.getWishes(pageable), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<WishDto>> getWishesByEmail(@AuthUser MemberDto memberDto, Pageable pageable) {
        return new ResponseEntity<>(wishService.getWishesByEmail(memberDto.email(), pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createWish(@AuthUser MemberDto memberDto, @RequestBody WishDto wishDto) {
        return new ResponseEntity<>(wishService.createWish(memberDto.email(), wishDto), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateWish(@AuthUser MemberDto memberDto, @PathVariable Long productId, @RequestBody WishDto wishDto) {
        return ResponseEntity.ok(wishService.updateWish(memberDto.email(), wishDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteWish(@AuthUser MemberDto memberDto, @PathVariable Long productId) {
        wishService.deleteWish(memberDto.email(), productId);
        return ResponseEntity.ok("Wish deleted.");
    }
}
