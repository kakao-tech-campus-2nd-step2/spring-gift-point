package gift.controller;

import gift.dto.pageDTO.PageRequestDTO;
import gift.dto.pageDTO.WishlistPageResponseDTO;
import gift.dto.wishlistDTO.WishlistRequestDTO;
import gift.dto.wishlistDTO.WishlistResponseDTO;
import gift.model.Member;
import gift.service.WishlistService;
import gift.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "위시리스트 관리 API", description = "위시리스트 관리를 위한 API")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    @Operation(summary = "상품을 위시리스트에 추가", description = "상품을 위시리스트에 추가합니다.")
    public ResponseEntity<WishlistResponseDTO> addWishlist(
        @RequestBody @Valid WishlistRequestDTO wishlistRequestDTO, @LoginMember Member member) {
        if (member == null) {
            return ResponseEntity.status(401).build();
        }
        WishlistResponseDTO wishlistResponseDTO = wishlistService.addWishlist(member.getEmail(),
            wishlistRequestDTO);
        return ResponseEntity.status(201).body(wishlistResponseDTO);
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "위시리스트 목록을 조회합니다.")
    public ResponseEntity<WishlistPageResponseDTO> getWishlist(
        @Valid PageRequestDTO pageRequestDTO, @LoginMember Member member
    ) {
        if (member == null) {
            return ResponseEntity.status(401).build();
        }
        Pageable pageable = PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size(),
            Sort.by(pageRequestDTO.sort()));
        WishlistPageResponseDTO wishlistPageResponseDTO = wishlistService.getWishlists(
            member.getEmail(), pageable);
        return ResponseEntity.ok(wishlistPageResponseDTO);
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트에서 상품 삭제", description = "위시리스트에서 상품을 삭제합니다.")
    public ResponseEntity<Void> removeWishlist(@PathVariable Long wishId,
        @LoginMember Member member) {
        if (member == null) {
            return ResponseEntity.status(401).build();
        }
        wishlistService.removeWishlist(wishId);
        return ResponseEntity.status(204).build();
    }
}