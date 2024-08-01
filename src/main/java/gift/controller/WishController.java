package gift.controller;

import gift.anotation.LoginMember;
import gift.domain.Member;
import gift.domain.Wish;
import gift.dto.WishDTO;
import gift.repository.MemberRepository;
import gift.service.WishService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "WishController", description = "위시 상품 관리 API")
public class WishController {

    private final WishService wishService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public WishController(WishService wishService, JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.wishService = wishService;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Operation(summary = "위시 리스트 조회", description = "회원의 위시 리스트를 페이지네이션하여 조회한다.")
    @GetMapping
    public ResponseEntity<Page<WishDTO>> getWishes(@LoginMember Member member, Pageable pageable) {
        Page<WishDTO> wishes = wishService.getWishes(member.getEmail(), pageable);
        return ResponseEntity.ok(wishes);
    }

    @Operation(summary = "위시 리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
    @PostMapping("/{productId}")
    public ResponseEntity<Void> addWish(@LoginMember Member member, @PathVariable Long productId) {
        wishService.addWish(member.getEmail(), productId);
        return ResponseEntity.status(201).build();
}

    @Operation(summary = "위시 리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeWish(@LoginMember Member member, @PathVariable Long productId) {
        wishService.removeWish(member.getEmail(), productId);
        return ResponseEntity.noContent().build();
    }
}
