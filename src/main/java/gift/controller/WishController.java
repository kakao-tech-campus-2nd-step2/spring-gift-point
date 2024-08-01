package gift.controller;

import static gift.util.constants.MemberConstants.INVALID_AUTHORIZATION_HEADER;
import static gift.util.constants.MemberConstants.INVALID_CREDENTIALS;
import static gift.util.constants.ProductConstants.PRODUCT_NOT_FOUND;
import static gift.util.constants.WishConstants.ALREADY_EXISTS;
import static gift.util.constants.WishConstants.PERMISSION_DENIED;
import static gift.util.constants.WishConstants.WISH_NOT_FOUND;
import static gift.util.constants.auth.TokenConstants.EXPIRED_TOKEN;
import static gift.util.constants.auth.TokenConstants.INVALID_TOKEN;

import gift.dto.wish.WishCreateRequest;
import gift.dto.wish.WishResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wish API", description = "위시 관리 API")
@RestController
@RequestMapping("/api/wishes")
@SecurityRequirement(name = "bearerAuth")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "(명세 통일) 위시리스트 조회", description = "회원의 위시리스트를 조회합니다.")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                responseCode = "401",
                description = "유효하지 않은 Authorization 헤더 또는 토큰",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            name = "유효하지 않은 Authorization 헤더",
                            value = "{\"error\": \"" + INVALID_AUTHORIZATION_HEADER + "\"}"
                        ),
                        @ExampleObject(name = "유효하지 않은 JWT 토큰", value = "{\"error\": \"" + INVALID_TOKEN + "\"}"),
                        @ExampleObject(name = "만료된 JWT 토큰", value = "{\"error\": \"" + EXPIRED_TOKEN + "\"}")
                    }
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "JWT 토큰으로 회원 찾기 실패",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + INVALID_CREDENTIALS + "\"}")
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @GetMapping
    public ResponseEntity<Page<WishResponse>> getWishlist(
        @RequestAttribute("memberId") Long memberId,
        @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<WishResponse> wishlist = wishService.getWishlistByMemberId(memberId, pageable);
        return ResponseEntity.ok(wishlist);
    }

    @Operation(summary = "(명세 통일) 위시리스트 추가", description = "회원의 위시리스트에 새로운 항목을 추가합니다.")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "201", description = "위시 추가 성공"),
            @ApiResponse(
                responseCode = "401",
                description = "유효하지 않은 Authorization 헤더 또는 토큰",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            name = "유효하지 않은 Authorization 헤더",
                            value = "{\"error\": \"" + INVALID_AUTHORIZATION_HEADER + "\"}"
                        ),
                        @ExampleObject(name = "유효하지 않은 JWT 토큰", value = "{\"error\": \"" + INVALID_TOKEN + "\"}"),
                        @ExampleObject(name = "만료된 JWT 토큰", value = "{\"error\": \"" + EXPIRED_TOKEN + "\"}")
                    }
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "JWT 토큰으로 회원 찾기 실패",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + INVALID_CREDENTIALS + "\"}")
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 상품 Id",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + PRODUCT_NOT_FOUND + "(상품 Id)\"}")
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "중복된 상품 추가",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + ALREADY_EXISTS + "\"}")
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<WishResponse> addWish(
        @Valid @RequestBody WishCreateRequest wishCreateRequest,
        @RequestAttribute("memberId") Long memberId
    ) {
        WishResponse createdWish = wishService.addWish(wishCreateRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWish);
    }

    @Operation(summary = "(명세 통일) 위시리스트 항목 삭제", description = "회원의 위시리스트에서 특정 항목을 삭제합니다.")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "204", description = "위시리스트 항목 삭제 성공"),
            @ApiResponse(
                responseCode = "401",
                description = "유효하지 않은 Authorization 헤더 또는 토큰",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            name = "유효하지 않은 Authorization 헤더",
                            value = "{\"error\": \"" + INVALID_AUTHORIZATION_HEADER + "\"}"
                        ),
                        @ExampleObject(name = "유효하지 않은 JWT 토큰", value = "{\"error\": \"" + INVALID_TOKEN + "\"}"),
                        @ExampleObject(name = "만료된 JWT 토큰", value = "{\"error\": \"" + EXPIRED_TOKEN + "\"}")
                    }
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "승인되지 않은 접근",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            name = "JWT 토큰으로 회원 찾기 실패",
                            value = "{\"error\": \"" + INVALID_CREDENTIALS + "\"}"
                        ),
                        @ExampleObject(name = "위시 삭제 권한 없음", value = "{\"error\": \"" + PERMISSION_DENIED + "\"}")
                    }
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 위시리스트 항목 Id",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + WISH_NOT_FOUND + "(위시 Id)\"}")
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
        @PathVariable("wishId") Long wishId,
        @RequestAttribute("memberId") Long memberId
    ) {
        wishService.deleteWish(wishId, memberId);
        return ResponseEntity.noContent().build();
    }
}
