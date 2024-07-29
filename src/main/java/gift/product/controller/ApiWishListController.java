package gift.product.controller;

import gift.product.dto.WishRequestDTO;
import gift.product.dto.WishResponseDTO;
import gift.product.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WishList", description = "API 컨트롤러")
@RestController
@RequestMapping("/api/wishlist")
public class ApiWishListController {

    private final WishListService wishListService;

    @Autowired
    public ApiWishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @Operation(
        summary = "위시 상품 목록",
        description = "유저의 위시 상품을 목록을 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "위시 상품 목록 조회 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Page.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "사용자 요청에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우"
        )
    })
    @GetMapping
    public Page<WishResponseDTO> showProductList(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable) {
        System.out.println("[ApiWishListController] showProductList()");
        return wishListService.getAllWishes(authorization, pageable);
    }

    @Operation(
        summary = "위시 상품 등록",
        description = "위시 상품을 등록합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "상품 등록 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = WishResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "등록하고자 하는 상품의 정보를 제대로 입력하지 않은 경우"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 상품을 위시 상품으로 등록을 시도한 경우"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "상품 등록 정보에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우"
        )
    })
    @PostMapping
    public ResponseEntity<WishResponseDTO> registerWishProduct(
        @RequestHeader("Authorization") String authorization,
        @Valid @RequestBody WishRequestDTO wishRequestDTO) {
        System.out.println("[ApiWishListController] registerWishProduct()");
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(wishListService.registerWishProduct(authorization, wishRequestDTO));
    }

    @Operation(
        summary = "위시 상품 삭제",
        description = "위시 상품을 삭제합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "위시 상품 삭제 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 위시 상품의 삭제를 시도한 경우"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "사용자 요청에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWishProduct(
        @RequestHeader("Authorization") String authorization,
        @PathVariable Long id) {
        System.out.println("[ApiWishListController] deleteWishProduct()");
        wishListService.deleteWishProduct(authorization, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
