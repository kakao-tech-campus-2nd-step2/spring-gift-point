package gift.wish.controller;

import gift.common.auth.JwtUtil;
import gift.common.util.CommonResponse;
import gift.wish.dto.WishDTO;
import gift.wish.dto.WishCreateRequest;
import gift.wish.dto.WishCreateResponse;
import gift.wish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "위시리스트 상품 API", description = "위시리스트 상품을 추가, 조회, 삭제하는 API")
public class WishController {

    private final WishService wishService;
    private final JwtUtil jwtUtil;

    public WishController(WishService wishService, JwtUtil jwtUtil) {
        this.wishService = wishService;
        this.jwtUtil = jwtUtil;
    }


    // 1. 사용자 위시리스트에 상품 추가
    @Operation(summary = "위시리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<?> createWish(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody WishCreateRequest wishRequest
    ) {
        System.out.println("authorizationHeader = " + authorizationHeader);
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : null;

        if (token == null || !jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(401).body(new CommonResponse<>(null, "Invalid or missing token", false));
        }

        WishCreateResponse wishCreateResponse = wishService.createWish(token, wishRequest.getProductId());

        return ResponseEntity.status(201).body(new CommonResponse<>(wishCreateResponse, "위시리스트에 상품이 추가되었습니다.", true));
    }

    // 2. 사용자의 위시리스트 삭제
    @Operation(summary = "위시리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
    @DeleteMapping("/{wishId}")
    public ResponseEntity<?> deleteWish(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long wishId
    ) {
        // 토큰 추출
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : null;

        if (token == null || !jwtUtil.isTokenValid(token)) {
            // 401 Unauthorized
            return ResponseEntity.status(401).body(new CommonResponse<>(null, "Invalid or missing token", false));
        }

        wishService.deleteWish(wishId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new CommonResponse<>(null, "위시리스트에서 상품이 삭제되었습니다.", true));
    }

    // 3. 전체 위시리스트 조회 (페이지네이션 적용)
    @Operation(summary = "위시리스트 상품 조회 (페이지네이션 적용)", description = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
    @GetMapping()
    public ResponseEntity<?> getWishlistByPagination(
                                      @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                      @RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam(defaultValue = "price,desc") String sort
    ) {
        // 토큰 추출
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : null;

        if (token == null || !jwtUtil.isTokenValid(token)) {
            // 401 Unauthorized
            return ResponseEntity.status(401).body(new CommonResponse<>(null, "Invalid or missing token", false));
        }

        // sort 파라미터를 ',' 기준으로 분리
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        String direction = sortParams.length > 1 ? sortParams[1] : "desc";

        // 페이지네이션 처리
        Page<WishDTO> wishList = wishService.getWishlistByPage(page, size, sortBy, direction);

        return ResponseEntity.ok(new CommonResponse<>(wishList, "전체 위시리스트 조회 성공", true));
    }
}
