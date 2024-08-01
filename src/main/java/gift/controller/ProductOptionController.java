package gift.controller;

import gift.dto.ProductOptionRequestDto;
import gift.dto.ProductOptionResponseDto;
import gift.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "ProductOptions", description = "상품 옵션 관련 API")
public class ProductOptionController {
    private final ProductOptionService productOptionService;

    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 추가", description = "새로운 상품 옵션을 추가합니다.",
            requestBody = @RequestBody(
                    description = "추가할 상품 옵션의 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ProductOptionRequestDto.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품 옵션이 성공적으로 추가되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ProductOptionResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 동일한 옵션이 이미 존재하는 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<ProductOptionResponseDto> addProductOption(@PathVariable Long productId, @RequestBody ProductOptionRequestDto productOptionRequestDto) {
        ProductOptionResponseDto createdProductOption = productOptionService.addProductOption(productId, productOptionRequestDto);
        return new ResponseEntity<>(createdProductOption, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품의 모든 옵션 조회", description = "특정 상품의 모든 옵션을 조회합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "productId",
                            description = "조회할 상품 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 옵션 목록이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ProductOptionResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<List<ProductOptionResponseDto>> getProductOptions(@PathVariable Long productId) {
        List<ProductOptionResponseDto> productOptions = productOptionService.getProductOptions(productId);
        return new ResponseEntity<>(productOptions, HttpStatus.OK);
    }

    @PutMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 수정", description = "기존 상품 옵션을 수정합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "productId",
                            description = "수정할 상품 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "optionId",
                            description = "수정할 옵션 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @RequestBody(
                    description = "수정할 상품 옵션의 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ProductOptionRequestDto.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 옵션이 성공적으로 수정되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ProductOptionResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 동일한 옵션이 이미 존재하는 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "상품 또는 옵션을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<ProductOptionResponseDto> updateProductOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody ProductOptionRequestDto productOptionRequestDto) {
        ProductOptionResponseDto updatedProductOption = productOptionService.updateProductOption(productId, optionId, productOptionRequestDto);
        return new ResponseEntity<>(updatedProductOption, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 삭제", description = "기존 상품 옵션을 삭제합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "productId",
                            description = "삭제할 상품 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "optionId",
                            description = "삭제할 옵션 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "상품 옵션이 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "상품 또는 옵션을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteProductOption(@PathVariable Long productId, @PathVariable Long optionId) {
        productOptionService.deleteProductOption(productId, optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
