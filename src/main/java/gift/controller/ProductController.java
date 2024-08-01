package gift.controller;

import gift.dto.ErrorResponseDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductWithOptionRequest;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@Tag(name = "Product", description = "상품 관리 API")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 목록 조회", description = "모든 상품의 정보를 페이지별로 조회한다. (카테고리로 필터링 가능)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort,
        @RequestParam(required = false) Long categoryId) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        if (categoryId != null) {
            Page<ProductResponseDto> productList = productService.findAllByCategoryId(categoryId,
                pageable);
            return new ResponseEntity<>(productList, HttpStatus.OK);
        }

        Page<ProductResponseDto> productList = productService.findAll(pageable);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @Operation(summary = "상품 단건 조회", description = "상품 ID를 받아, 해당하는 상품을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공", content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "상품 추가", description = "새로운 상품을 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<String> createProduct(
        @RequestBody @Valid ProductWithOptionRequest productWithOptionRequest) {
        productService.save(productWithOptionRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "상품 정보 수정", description = "상품 ID를 받아, 해당하는 상품 정보를 수정한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id,
        @RequestBody @Valid ProductRequestDto productRequestDto) {
        productService.update(id, productRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "상품 삭제", description = "상품 ID를 받아, 해당하는 상품을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}