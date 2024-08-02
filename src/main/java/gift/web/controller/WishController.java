package gift.web.controller;

import gift.service.wish.WishService;
import gift.web.dto.MemberDto;
import gift.web.dto.wish.WishRequestDto;
import gift.web.dto.wish.WishResponseDto;
import gift.web.jwt.AuthUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<WishResponseDto>> getWishes(@AuthUser MemberDto memberDto, Pageable pageable) {
        // todo : 관리자 권한 검증 로직 구현
        return new ResponseEntity<>(wishService.getWishes(pageable), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> getWishesByEmail(@AuthUser MemberDto memberDto, @PageableDefault(sort = "name") Pageable pageable) {
        return new ResponseEntity<>(wishService.getWishesByEmail(memberDto.email(), pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createWish(@AuthUser MemberDto memberDto, @RequestBody @Valid  WishRequestDto wishRequestDto) {
        return new ResponseEntity<>(wishService.createWish(memberDto.email(), wishRequestDto), HttpStatus.CREATED);
    }

    /** @Depreacated 위시에 count가 삭제됨
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateWish(@AuthUser MemberDto memberDto, @PathVariable Long productId, @RequestBody WishRequestDto wishRequestDto) {
        return ResponseEntity.ok(wishService.updateWish(memberDto.email(), wishRequestDto));
    }
     **/

    @DeleteMapping("/{wishId}")
    public ResponseEntity<?> deleteWish(@AuthUser MemberDto memberDto, @PathVariable Long wishId) {
        wishService.deleteWish(memberDto.email(), wishId);
        return ResponseEntity.noContent().build();
    }
}
