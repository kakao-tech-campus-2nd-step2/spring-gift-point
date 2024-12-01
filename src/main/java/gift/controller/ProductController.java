package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductUpdateDto;
import gift.service.ProductService;
import gift.vo.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 관리", description = "상품 조회, 추가, 업데이트, 삭제와 관련된 API들을 제공합니다.")
public class ProductController {

    private final ProductService service;

    private final ProductService productService;

    public ProductController(ProductService service, ProductService productService) {
        this.service = service;
        this.productService = productService;
    }

    /**
     *
     * @param pageable Pageable
     *                 page 요청 시 페이지 번호 지정, 기본값 0
     *                 size 요청 시 페이지 크기 지정, 기본값 10
     * @return 페이징된 상품 목록 객체 반환
     */
    @GetMapping("/all")
    @Operation(
            summary = "전체 상품 조회 (페이징)",
            description = "페이지 번호와 페이지 크기를 사용하여 페이징된 상품 전체 목록을 조회하는 API입니다."
    )
    @Parameter(name = "pageable", description = "페이지 관련 정보", example = "page=0&size=10&sort=name,asc&categoryId=1")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<Product> allProductsPaged = service.getAllProducts(pageable);

        return ResponseEntity.ok().body(allProductsPaged.map(ProductResponseDto::toProductResponseDto));
    }

    /**
     *
     * @param categoryId 조회할 카테고리 ID
     * @param pageable Pageable
     *                 - page: 요청 시 페이지 번호 지정, 기본값 0
     *                 - size 요청 시 페이지 크기 지정, 기본값 10
     *                 - sort 정렬 기준 지정 (ex: name,asc)
     * @return 카테고리별로 페이징된 상품 목록 반환. 정렬도 포함
     */
    @GetMapping()
    @Operation(
            summary = "카테고리별 상품 조회(페이징)(정렬)",
            description = "페이지 번호와 페이지 크기를 사용하여 카테고리별로 조회된 상품 전체 목록을 조회하는 API입니다. 정렬 역시 적용 가능합니다. "
    )
    @Parameters({
            @Parameter(name = "pageable", description = "페이지 관련 정보", example = "page=0&size=10&sort=name,asc"),
            @Parameter(name = "categoryId", description = "카테고리 ID", example = "1")
    })
    public ResponseEntity<Page<ProductResponseDto>> getAllProductsByCategory(
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam("categoryId") Long categoryId
        ) {
        Page<Product> allProductsByCategoryPaged = productService.getProductsByCategory(categoryId, pageable);

        return ResponseEntity.ok().body(allProductsByCategoryPaged.map(ProductResponseDto::toProductResponseDto));
    }

    /**
     * 상품 조회 - 한 개
     * @param productId 조회할 상품의 ID
     * @return 조회한 product
     */
    @GetMapping("/{productId}")
    @Operation(
            summary = "상품 조회(개별)",
            description = "주어진 ID에 해당하는 상품 하나를 조회하는 API입니다."
    )
    @Parameter(name = "productId", description = "조회할 상품의 ID", required = true, example = "1")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable(value = "productId") Long productId) {
        Product product = service.getProductById(productId);
        ProductResponseDto productResponseDto = ProductResponseDto.toProductResponseDto(product);

        return ResponseEntity.ok().body(productResponseDto);
    }

    /**
     * 상품 추가
     * @param productRequestDto Dto로 받음
     * @return ResponseEntity로 Response
     */
    @PostMapping()
    @Operation(
            summary = "상품 추가",
            description = "새로운 상품을 추가하는 API입니다. 추가할 상품은 반드시 하나의 카테고리와 하나 이상의 옵션이 포함되어야 합니다."
    )
    @Parameter(name = "productRequestDto", description = "상품 추가에 필요한 정보가 담긴 DTO", required = true)
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        service.addProduct(productRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 수정
     * @param productUpdateDto 수정할 상품 (JSON 형식)
     *    ㄴ받는 Product에 id 필드 값이 포함 되어 있어야 한다.
     * @return 수정된 상품
     */
    @PutMapping()
    @Operation(
            summary = "상품 수정",
            description = "기존 상품을 수정하는 API입니다. 요청된 JSON 데이터에 상품 ID가 포함되어 있어야 합니다."
    )
    @Parameter(name = "productUpdateDto", description = "수정할 상품 정보가 담긴 DTO", required = true)
    public ResponseEntity<Void> updateProduct(@Valid @RequestBody ProductUpdateDto productUpdateDto) {
        service.updateProduct(productUpdateDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 삭제
     * @param productId 삭제할 상품의 ID
     * @return HTTP State - No Content
     */
    @DeleteMapping("/{productId}")
    @Operation(
            summary = "상품 삭제",
            description = "주어진 ID에 해당하는 상품을 삭제하는 API입니다."
    )
    @Parameter(name = "productId", description = "삭제할 상품의 ID", example = "1")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "productId") Long productId) {
        System.out.println("productId " + productId);
        service.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
