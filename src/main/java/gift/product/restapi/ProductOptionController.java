package gift.product.restapi;

import gift.advice.ErrorResponse;
import gift.core.domain.product.ProductOptionService;
import gift.product.restapi.dto.request.ProductOptionRegisterRequest;
import gift.product.restapi.dto.request.ProductOptionUpdateRequest;
import gift.product.restapi.dto.response.ListedProductOptionResponse;
import gift.product.restapi.dto.response.ProductOptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 옵션")
public class ProductOptionController {
    private final ProductOptionService productOptionService;

    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @GetMapping("/{productId}/options")
    @Operation(
            summary = "상품 옵션 목록 조회",
            description = "상품 옵션 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "productId", description = "상품 ID")
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "상품 옵션 목록을 조회합니다.",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductOptionResponse.class)))
    )
    public ListedProductOptionResponse getOptions(
            @PathVariable("productId") Long productId
    ) {
        return ListedProductOptionResponse.from(productOptionService.getOptionsFromProduct(productId));
    }

    @PostMapping("/{productId}/options")
    @Operation(
            summary = "상품 옵션 등록",
            description = "상품에 옵션을 등록합니다.",
            parameters = {
                    @Parameter(name = "productId", description = "상품 ID")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품에 옵션을 등록합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "상품에 옵션을 등록할 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "상품이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public void registerOption(
            @PathVariable Long productId,
            @RequestBody ProductOptionRegisterRequest request
    ) {
        productOptionService.registerOptionToProduct(productId, request.toDomain());
    }

    @PutMapping("/{productId}/options/{optionId}")
    @Operation(
            summary = "상품 옵션 수정",
            description = "상품의 옵션을 수정합니다.",
            parameters = {
                    @Parameter(name = "productId", description = "상품 ID"),
                    @Parameter(name = "optionId", description = "옵션 ID")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품의 옵션을 수정합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "상품의 옵션을 수정할 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "상품이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "옵션이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))

                    )
            }
    )
    public void updateOption(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            @RequestBody ProductOptionUpdateRequest request
    ) {
        productOptionService.updateOption(productId, request.toDomainWithId(optionId));
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(
            summary = "상품 옵션 삭제",
            description = "상품에서 옵션을 삭제합니다.",
            parameters = {
                    @Parameter(name = "productId", description = "상품 ID"),
                    @Parameter(name = "optionId", description = "옵션 ID")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품에서 옵션을 삭제합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "상품에서 옵션을 삭제할 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "상품이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "옵션이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))

                    )
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "상품에서 옵션을 삭제합니다."
    )
    public void deleteOption(
            @PathVariable Long productId,
            @PathVariable Long optionId
    ) {
        productOptionService.removeOptionFromProduct(productId, optionId);
    }
}
