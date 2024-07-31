package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.Member;
import gift.domain.Wish;
import gift.dto.PageRequestDto;
import gift.dto.WishDto;
import gift.dto.WishResponseDto;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wish", description = "위시리스트 API")
public class WishController {
    private final WishService wishService;
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    @Operation(summary = "위시 리스트 상품 조회 (페이지네이션 적용)", description = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Page<WishResponseDto>> getWishlist(
        @Parameter(hidden = true) @LoginMember Member member,
        @Valid @ModelAttribute PageRequestDto pageRequestDto) {

        Pageable pageable = pageRequestDto.toPageable();
        Page<WishResponseDto> wishItems = wishService.findByMemberId(member.getId(), pageable);
        return ResponseEntity.ok(wishItems);
    }

    @PostMapping
    @Operation(summary = "위시 리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<String> addWish(@Valid @RequestBody WishDto wishDto, @Parameter(hidden = true)@LoginMember Member member) {
        wishService.addWish(member.getId(), wishDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("위시 추가 성공");
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<String> deleteWish(@PathVariable("wishId") Long wishId, @Parameter(hidden = true)@LoginMember Member member) {
        wishService.deleteWish(wishId);
        return ResponseEntity.status(HttpStatus.OK).body("위시 삭제 성공");
    }

}
