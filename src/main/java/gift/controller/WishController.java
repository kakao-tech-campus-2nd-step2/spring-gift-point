package gift.controller;

import gift.config.LoginMember;
import gift.domain.member.Member;
import gift.dto.ProductDto;
import gift.dto.request.WishCreateRequest;
import gift.dto.response.ErrorResponse;
import gift.dto.response.ProductResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wish API", description = "위시(장바구니) 관련 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/wishes")
@RestController
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시 리스트 조회", description = "로그인한 사용자의 위시 리스트를 페이지별로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시 리스트 조회 성공", content = @Content(schema = @Schema(implementation = PagedModel.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> productList(@Parameter(hidden = true) @LoginMember Member member,
                                                             @ParameterObject @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<ProductDto> productDtoList = wishService.getProducts(member, pageable);

        List<ProductResponse> productResponseList = productDtoList.stream()
                .map(ProductDto::toResponseDto)
                .toList();

        PageImpl<ProductResponse> response = new PageImpl<>(productResponseList, pageable, productResponseList.size());

        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "위시 리스트에 상품 추가", description = "로그인한 사용자의 위시 리스트에 상품을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품이 성공적으로 추가됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "이미 위시 리스트에 존재하는 상품")
    })
    @PostMapping
    public ResponseEntity<Void> productAdd(@Parameter(hidden = true) @LoginMember Member member,
                                           @Parameter(description = "위시 리스트에 추가할 상품의 정보", required = true) @RequestBody @Valid WishCreateRequest request) {
        wishService.addProduct(member, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "위시 리스트에서 상품 제거", description = "로그인한 사용자의 위시 리스트에서 상품을 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "상품이 성공적으로 제거됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없거나 이미 위시리스트에 존재하지 않는 상품"),
    })
    @DeleteMapping
    public ResponseEntity<Void> productRemove(@Parameter(hidden = true) @LoginMember Member member,
                                              @Parameter(description = "위시 리스트에서 제거할 상품의 정보", required = true) @RequestBody @Valid WishCreateRequest request) {
        wishService.removeProduct(member, request.getProductId());

        return ResponseEntity.noContent()
                .build();
    }

}
