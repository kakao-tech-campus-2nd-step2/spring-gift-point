package gift.controller;

import gift.auth.JwtUtil;
import gift.dto.WishResponseDto;
import gift.dto.WishRequestDto;
import gift.service.WishlistService;
import gift.vo.Wish;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "위시리스트 관리", description = "위시리스트 조회, 추가, 삭제와 관련된 API들을 제공합니다.")
public class WishlistController {

    private final WishlistService service;
    private final JwtUtil jwtUtil;

    public WishlistController(WishlistService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    @Operation(
            summary = "위시리스트 조회",
            description = "회원의 위시리스트를 페이징하여 조회하는 API입니다."
    )
    @Parameters({
            @Parameter(name = "authorizationHeader", description = "회원 인증을 위한 Authorization 헤더", required = true),
            @Parameter(name = "pageable", description = "페이지 관련 정보", example = "page=0&size=10&sort=createdDate,desc"),
    })
    public ResponseEntity<Page<WishResponseDto>> getWishProductList(
            @RequestHeader("Authorization") String authorizationHeader,
            @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Long memberId = jwtUtil.getMemberIdFromAuthorizationHeader(authorizationHeader);

        Page<Wish> allWishlistsPaged = service.getWishProductList(memberId, pageable);

        return ResponseEntity.ok().body(allWishlistsPaged.map(WishResponseDto::toWishDto));
    }

    @PostMapping()
    @Operation(
            summary = "위시리스트에 상품 추가",
            description = "주어진 ID에 해당하는 특정 상품을 회원의 위시리스트에 추가하는 API입니다."
    )
    @Parameters({
            @Parameter(name = "wishRequestDto", description = "위시리스트에 추가할 상품의 ID를 담은 DTO", required = true),
            @Parameter(name = "authorizationHeader", description = "회원 인증을 위한 Authorization 헤더", required = true)
    })
    public ResponseEntity<Void> addToWishlist(@RequestBody @Valid WishRequestDto wishRequestDto, @RequestHeader("Authorization") String authorizationHeader) {
        Long memberId = jwtUtil.getMemberIdFromAuthorizationHeader(authorizationHeader);

        service.addWishProduct(memberId, wishRequestDto.productId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{wishId}")
    @Operation(
            summary = "위시리스트에서 상품 삭제",
            description = "위시리스트에서 주어진 ID에 해당하는 특정 상품을 삭제하는 API입니다."
    )
    @Parameters({
            @Parameter(name = "wishId", description = "위시리스트에서 삭제할 상품의 ID", required = true),
            @Parameter(name = "authorizationHeader", description = "회원 인증을 위한 Authorization 헤더", required = true)
    })
    public ResponseEntity<Void> deleteToWishlist(@PathVariable("wishId") Long wishId, @RequestHeader("Authorization") String authorizationHeader) {
        service.deleteWishProduct(wishId);

        return ResponseEntity.ok().build();
    }

}
