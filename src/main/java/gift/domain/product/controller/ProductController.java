package gift.domain.product.controller;

import gift.domain.option.dto.OptionRequest;
import gift.domain.option.dto.OptionsResponse;
import gift.domain.option.service.OptionService;
import gift.domain.product.dto.ProductDetailResponse;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductResponse;
import gift.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/products")
@Tag(name = "ProductController", description = "Product API")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @Operation(summary = "상품 전체 조회", description = "상품 전체를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description = "요청에 성공하였습니다.(Page 내부 값은 ProductResponse 입니다.)", content = @Content(schema = @Schema(implementation = Page.class), mediaType = "application/json")),
        @ApiResponse(responseCode="400", description = "잘못된 요청", content = @Content(mediaType = "text/plain;charset=UTF-8\n")),
        @ApiResponse(responseCode="500", description = "서버 오류", content = @Content(mediaType = "text/plain;charset=UTF-8\n"))
    })
    @GetMapping()
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false) Long categoryId) {
        Page<ProductResponse> response = productService.getAllProducts(pageable, categoryId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 상품 조회", description = "id 값의 상품을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description = "요청에 성공하였습니다.", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
        @ApiResponse(responseCode="400", description = "잘못된 요청", content = @Content(mediaType = "text/plain;charset=UTF-8\n")),
        @ApiResponse(responseCode="500", description = "서버 오류", content = @Content(mediaType = "text/plain;charset=UTF-8\n"))
    })
    @Parameter(name = "productId", description = "조회할 상품의 ID", example = "1")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable("productId") Long id) {
        ProductDetailResponse response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "상품 생성", description = "관리자가 상품을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @PostMapping()
    public ResponseEntity<Void> createProduct(
        @RequestBody @Valid ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "상품 수정", description = "해당 상품을 수정합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "productId", description = "수정할 상품의 ID", example = "1")
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(
        @PathVariable("productId") Long id,
        @RequestBody @Valid ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "상품 삭제", description = "해당 상품을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.")
    @Parameter(name = "productId", description = "삭제할 상품의 ID", example = "1")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /*
    옵션 API
    */

    @Operation(summary = "해당 상품에 대한 옵션들 전체 조회", description = "id 값으로 지정된 상품에 대한 옵션들을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description = "요청에 성공하였습니다.", content = @Content(schema = @Schema(implementation = OptionsResponse.class), mediaType = "application/json")),
        @ApiResponse(responseCode="400", description = "잘못된 요청", content = @Content(mediaType = "text/plain;charset=UTF-8\n")),
        @ApiResponse(responseCode="500", description = "서버 오류", content = @Content(mediaType = "text/plain;charset=UTF-8\n"))
    })
    @Parameter(name = "ProductId", description = "옵션을 조회할 상품의 ID", example = "1")
    @GetMapping("/{productId}/options")
    public ResponseEntity<OptionsResponse> getProductOptions(@PathVariable("productId") Long id) {
        OptionsResponse options = new OptionsResponse(optionService.getProductOptions(id));
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "해당 상품에 대한 옵션 추가", description = "id 값으로 지정된 상품에 대한 옵션을 추가합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "productId", description = "옵션을 추가할 상품의 ID", example = "1")
    @PostMapping("/{productId}/options")
    public ResponseEntity<Void> addOptionToProduct(
        @PathVariable("productId") Long id,
        @Valid @RequestBody OptionRequest request) {
        optionService.addOptionToProduct(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "상품 옵션 수정", description = "상품에 대한 옵션을 수정합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "productId", description = "해당 상품의 ID", example = "1")
    @Parameter(name = "optionId", description = "수정할 옵션의 ID", example = "1")
    @PutMapping("{productId}/options/{optionId}")
    public ResponseEntity<Void> updateProductOption(
        @PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId,
        @RequestBody OptionRequest request
    ){
        optionService.updateProductOption(productId, optionId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "상품 옵션 삭제", description = "상품에 대한 옵션을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "productId", description = "해당 상품의 ID", example = "1")
    @Parameter(name = "optionId", description = "삭제할 옵션의 ID", example = "1")
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteProductOption(@PathVariable("productId") Long productId, @PathVariable("optionId") Long optionId){
        optionService.deleteProductOption(productId, optionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
