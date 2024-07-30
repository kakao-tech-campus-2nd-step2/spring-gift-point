package gift.controller;

import gift.model.Member;
import gift.model.Wishlist;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Tag(name = "Wishlist", description = "위시리스트 관련 api")
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public WishlistController(WishlistService wishlistService, MemberRepository memberRepository, MemberService memberService) {
        this.wishlistService = wishlistService;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @GetMapping
    @Operation(summary = "회원의 모든 위시리스트 조회", description = "회원의 모든 위시리스트를 조회합니다.")
    public ResponseEntity<Page<Wishlist>> getWishlist(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        String token = authHeader.substring(7);
        memberService.isTokenBlacklisted(token);
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        Page<Wishlist> wishlistPage = wishlistService.getWishlist(member.getId(), pageable);
        return ResponseEntity.ok(wishlistPage);
    }

    @PostMapping("/{productId}")
    @Operation(summary = "상품 id로 위시리스트 추가", description = "상품 id로 위시리스트 추가합니다.")
    public ResponseEntity<Void> addProductToWishlist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable Long productId) {
        String token = authHeader.substring(7);
        memberService.isTokenBlacklisted(token);
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        wishlistService.addProductToWishlist(member.getId(), productId);
        return ResponseEntity.created(URI.create("/api/wishlist/" + productId)).build();
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 id로 위시리스트 삭제", description = "상품 id로 위시리스트 삭제합니다.")
    public ResponseEntity<Void> removeProductFromWishlist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable Long productId) {
        String token = authHeader.substring(7);
        memberService.isTokenBlacklisted(token);
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        wishlistService.removeProductFromWishlist(member.getId(), productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
