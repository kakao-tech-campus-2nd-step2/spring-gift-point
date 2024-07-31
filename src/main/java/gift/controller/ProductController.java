package gift.controller;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.dto.request.OptionCreateRequest;
import gift.dto.request.ProductCreateRequest;
import gift.dto.request.ProductUpdateRequest;
import gift.dto.response.ErrorResponse;
import gift.dto.response.OptionResponse;
import gift.dto.response.ProductResponse;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product API", description = "상품 관련 API")
@RequestMapping("/api/products")
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 목록 조회", description = "모든 상품의 목록을 페이지별로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @Content(schema = @Schema(implementation = PagedModel.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> productList(@ParameterObject @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,

                                                             @Parameter(description = "필터링 적용할 카테고리 ID (예: 1)")
                                                             @RequestParam(required = false) Long categoryId) {
        List<ProductDto> productDtoList = productService.getProducts(categoryId, pageable);

        List<ProductResponse> productResponseList = productDtoList.stream()
                .map(ProductDto::toResponseDto)
                .toList();

        PageImpl<ProductResponse> response = new PageImpl<>(productResponseList, pageable, productResponseList.size());

        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "상품 조회", description = "ID를 통해 특정 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 조회 성공", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> productOne(@Parameter(description = "조회할 상품의 ID", required = true) @PathVariable Long productId) {
        ProductDto productDto = productService.getProduct(productId);

        ProductResponse response = productDto.toResponseDto();

        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<Void> productAdd(@RequestBody @Valid ProductCreateRequest request) {
        List<OptionDto> optionDtoList = request.getOptions().stream()
                .map(OptionCreateRequest::toDto)
                .toList();
        ProductDto productDto = new ProductDto(request.getName(), request.getPrice(), request.getImageUrl(), request.getCategoryId(), optionDtoList);

        productService.addProduct(productDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "카테고리 또는 상품을 찾을 수 없음")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<Void> productEdit(@Parameter(description = "수정할 상품의 ID", required = true) @PathVariable Long productId,
                                            @RequestBody @Valid ProductUpdateRequest request) {
        ProductDto productDto = new ProductDto(request.getName(), request.getPrice(), request.getImageUrl(), request.getCategoryId());

        productService.editProduct(productId, productDto);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 삭제", description = "기존 상품을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> productRemove(@Parameter(description = "삭제할 상품의 ID", required = true) @PathVariable Long productId) {
        productService.removeProduct(productId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품의 모든 옵션을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "옵션 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = OptionResponse.class)))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponse>> optionList(@Parameter(description = "옵션을 조회할 상품의 ID", required = true) @PathVariable Long productId) {
        List<gift.dto.OptionDto> options = productService.getOptions(productId);

        List<OptionResponse> response = options.stream()
                .map(gift.dto.OptionDto::toResponseDto)
                .toList();

        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "상품 옵션 추가", description = "특정 상품에 옵션을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "옵션 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "중복된 옵션명으로 인한 등록 실패")
    })
    @PostMapping("/{productId}/options")
    public ResponseEntity<Void> optionAdd(@Parameter(description = "옵션이 추가될 상품의 ID", required = true) @PathVariable Long productId,
                                          @RequestBody @Valid OptionCreateRequest request) {
        gift.dto.OptionDto optionDto = new gift.dto.OptionDto(request.getName(), request.getQuantity());

        productService.addOption(productId, optionDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "상품 옵션 삭제", description = "특정 상품의 옵션을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "상품의 옵션이 하나 남았을 때 삭제 시 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품 또는 옵션을 찾을 수 없음")
    })
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> optionRemove(@Parameter(description = "상품 ID", required = true) @PathVariable Long productId,
                                             @Parameter(description = "옵션 ID", required = true) @PathVariable Long optionId) {
        productService.removeOption(productId, optionId);

        return ResponseEntity.noContent().build();
    }

}
