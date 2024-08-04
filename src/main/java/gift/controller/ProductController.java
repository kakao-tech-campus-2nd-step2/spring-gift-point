package gift.controller;


import gift.dto.ErrorResponse;
import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.dto.ProductResponseDTO;
import gift.dto.ProductUpdateRequest;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.ProductFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product(상품)", description = "Product관련 API입니다.")
public class ProductController {

    ProductFacadeService productService;

    //생성자 주입

    public ProductController(ProductFacadeService productService) {
        this.productService = productService;
    }


    @GetMapping("/{productId}")
    @Operation(summary = "ID로 Product 조회", description = "Product의 Id로 상품의 정보를 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공", content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    public ResponseEntity<ProductResponseDTO> getProductById(
        @Parameter(name = "productId", description = "조회할 상품의 id", example = "1")
        @PathVariable("productId") long productId) {
        ProductResponseDTO productResponse = new ProductResponseDTO(
            productService.getProductById(productId));

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    //Product Pagination
    @GetMapping
    @Operation(summary = "Product를 Page로 조회", description = "여러개의 Product를 페이지네이션 하여 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PagedModel.class)))),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    public ResponseEntity<Page<ProductResponseDTO>> getProductPage(
        @ParameterObject @PageableDefault(page=0, size=10, sort="id") Pageable pageable,
        @Parameter(description = "필터링 적용할 카테고리 ID, 없을 시 전체 상품 조회")
        @RequestParam(required = false) Long categoryId
    ) {
        Page<Product> products = productService.getProductPage(pageable,categoryId);
        Page<ProductResponseDTO> response = products.map(ProductResponseDTO::new);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //product 추가
    @PostMapping
    @Operation(summary = "Product 추가", description = "새로운 Product를 추가합니다. Product는 최소 1개의 Option을 가지고 있어야 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품 카테고리", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "409", description = "상품 이름 중복 ", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    public ResponseEntity<String> addProduct(@RequestBody @Valid ProductDTO productDTO) {
        Category category = productService.findCategoryById(productDTO.getCategoryId());
        Product product = productDTO.toEntity(category);
        List<Option> options = new ArrayList<>();
        for (OptionDTO optionDTO : productDTO.getOptions()) {
            options.add(optionDTO.toEntity(product));
        }
        productService.addProduct(product, options);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);

    }


    //product 수정
    @PutMapping("/{productId}")
    @Operation(summary = "Product 수정", description = "id에 해당하는 Product를 새로운 정보로 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "수정하려는 상품 조회 실패.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),})
    public ResponseEntity<String> editProduct(
        @Parameter(name = "productId", description = "Product Id", example = "1") @PathVariable("productId") Long productId,
        @Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        Category category = productService.findCategoryById(productUpdateRequest.getCategoryId());
        Product product = productUpdateRequest.toEntity(category);
        productService.updateProduct(product, productId);

        return new ResponseEntity<>("상품 수정 성공", HttpStatus.NO_CONTENT);

    }

    //product 삭제
    @DeleteMapping("/{productId}")
    @Operation(summary = "Product 삭제", description = "id에 해당하는 Product를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product"),
        @ApiResponse(responseCode = "404", description = "삭제하려는 상품 조회 실패.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
    })
    public ResponseEntity<String> deleteProduct(
        @Parameter(name = "productId", description = "Product Id", example = "1") @PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("상품 삭제 성공", HttpStatus.NO_CONTENT);
    }


}
