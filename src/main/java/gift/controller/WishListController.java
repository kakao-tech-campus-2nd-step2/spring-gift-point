package gift.controller;

import gift.model.AuthInfo;
import gift.model.WishListDTO;
import gift.model.WishListRequest;
import gift.model.WishListResponse;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    @Operation(summary = "특정 멤버의 위시리스트를 조회합니다.",
        description = "특정 멤버를 인증하기 위해 헤더에 JWT 토큰 값을 추가합니다."
            + "또한 쿼리스트링으로 오프셋 페이지네이션을 지원합니다.",
        parameters = {
            @Parameter(
                name = "Authorization",
                description = "JWT 토큰값",
                required = true,
                in = ParameterIn.HEADER,
                schema = @Schema(type = "string"),
                example = "Bearer some_jwt_token"
            )
        }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = WishListResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않습니다.", content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 에러입니다.", content = @Content)}
    )
    public ResponseEntity<?> getWishList(
        @PageableDefault(page = 0, size = 20, sort = "createdDate", direction = Sort.Direction.ASC)
        Pageable pageable, AuthInfo authInfo) {
        long memberId = authInfo.id();
        return ResponseEntity.ok().body(wishListService.getWishList(memberId, pageable));
    }

    @PostMapping
    @Operation(
        summary = "위시리스트 생성",
        description = "새로운 위시리스트 항목을 생성합니다. 헤더에 JWT 토큰 값을 추가합니다.",
        parameters = {
            @Parameter(
                name = "Authorization",
                description = "JWT 토큰값",
                required = true,
                in = ParameterIn.HEADER,
                schema = @Schema(type = "string"),
                example = "Bearer some_jwt_token"
            )
        }
    )

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = WishListResponse.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않습니다.", content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 에러입니다.", content = @Content)
    })
    public ResponseEntity<?> createWishList(
        @Parameter(description = "추가할 위시 리스트 정보", required = true)
        @RequestBody WishListRequest wishListRequest,
        AuthInfo authInfo) {
        long memberId = authInfo.id();
        WishListResponse response = wishListService.createWishList(
            new WishListDTO(memberId, wishListRequest.productId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @Operation(
        summary = "위시리스트 수량 수정",
        description = "위시리스트 항목의 수량을 수정합니다. 헤더에 JWT 토큰 값을 추가합니다.",
        parameters = {
            @Parameter(
                name = "Authorization",
                description = "JWT 토큰값",
                required = true,
                in = ParameterIn.HEADER,
                schema = @Schema(type = "string"),
                example = "Bearer some_jwt_token"
            )
        }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = WishListDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않습니다.", content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 에러입니다.", content = @Content)
    })

    public ResponseEntity<?> updateWishListQuantity(
        @Parameter(description = "업데이트할 위시 리스트 정보", required = true)
        @RequestBody WishListDTO wishListDTO,
        AuthInfo authInfo) {
        long memberId = authInfo.id();
        WishListDTO updatedWishList = wishListService.updateWishListQuantity(
            new WishListDTO(memberId, wishListDTO.productId()));
        return ResponseEntity.status(HttpStatus.OK).body(updatedWishList);
    }

    @DeleteMapping
    @Operation(
        summary = "위시리스트 항목 삭제",
        description = "위시리스트 항목을 삭제합니다. 헤더에 JWT 토큰 값을 추가합니다.",
        parameters = {
            @Parameter(
                name = "Authorization",
                description = "JWT 토큰값",
                required = true,
                in = ParameterIn.HEADER,
                schema = @Schema(type = "string"),
                example = "Bearer some_jwt_token"
            ),
            @Parameter(
                name = "productId",
                description = "삭제할 위시리스트 항목의 상품 ID",
                required = true,
                schema = @Schema(type = "long"),
                example = "1"
            )
        }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공", content = @Content),
        @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않습니다.", content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 에러입니다.", content = @Content)
    })
    public ResponseEntity<?> deleteWishList(
        @Parameter(description = "삭제할 위시리스트 항목의 상품 ID", required = true)
        @RequestParam long productId,
        AuthInfo authInfo) {
        long memberId = authInfo.id();
        wishListService.deleteWishList(memberId, productId);
        return ResponseEntity.noContent().build();
    }
}
