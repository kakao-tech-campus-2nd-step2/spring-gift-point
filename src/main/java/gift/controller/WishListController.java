package gift.controller;

import gift.annotation.KakaoUser;
import gift.dto.user.KakaoUserDTO;
import gift.dto.wish.WishPageResponseDTO;
import gift.dto.wish.WishRequestDTO;
import gift.dto.wish.WishResponseDTO;
import gift.service.ProductService;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/wishes")
public class WishListController {
    private final WishService wishService;
    private final ProductService productService;

    public WishListController(WishService wishService, ProductService productService) {
        this.wishService = wishService;
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회",
            description = "카카오 사용자의 위시리스트를 조회합니다.",
            responses = {
                @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<WishPageResponseDTO> getWishlist(@KakaoUser KakaoUserDTO kakaoUserDTO, @PageableDefault(sort = "name") Pageable pageable) {
        WishPageResponseDTO content = wishService.getWishlist(kakaoUserDTO.user().getId(), pageable);

        return new ResponseEntity<>(content, HttpStatus.OK);
    }


    @PostMapping
    @Operation(summary = "위시리스트 추가",
            description = "카카오 사용자의 위시리스트에 상품을 추가합니다.",
            responses = {
                @ApiResponse(responseCode = "201", description = "위시리스트 추가 성공"),
                @ApiResponse(responseCode = "400", description = "잘못된 값 입력"),
                @ApiResponse(responseCode = "401", description = "인증 오류"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<WishResponseDTO> addWishOption(@KakaoUser KakaoUserDTO kakaoUserDTO, @Valid @RequestBody WishRequestDTO wishRequestDTO) {
        WishResponseDTO wishResponseDTO = wishService.addWishOption(kakaoUserDTO.user().getId(), wishRequestDTO);

        return new ResponseEntity<>(wishResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트 삭제",
            description = "카카오 사용자의 위시리스트에서 상품을 삭제합니다.",
            responses = {
                @ApiResponse(responseCode = "204", description = "위시리스트 삭제 성공"),
                @ApiResponse(responseCode = "401", description = "인증 오류"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 또는 상품"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<Void> deleteWishProduct(@KakaoUser KakaoUserDTO kakaoUserDTO, @PathVariable(name = "wishId") Long wishId) {
        wishService.deleteWishOption(kakaoUserDTO.user().getId(), wishId);

        return ResponseEntity.noContent().build();
    }
}
