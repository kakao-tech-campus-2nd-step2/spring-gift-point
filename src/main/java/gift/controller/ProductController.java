package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductUpdateDto;
import gift.service.ProductService;
import gift.vo.Product;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    /**
     * 상품 조회 -전체 (페이징)
     * 페이지 번호와 페이지 크기를 사용하여 페이징된 상품 목록을 조회한다.
     * @param pageNumber 요청 시 페이지 번호 지정, 기본값 0
     * @param pageSize 요청 시 페이지 크기 지정, 기본값 10
     * @return 페이징된 상품 목록, 관련 메타데이터 포함한 응답 객체 반환
     */
    @GetMapping()
    @Operation(
            summary = "전체 상품 조회 (페이징)",
            description = "페이지 번호와 페이지 크기를 사용하여 페이징된 상품 전체 목록을 조회하는 API입니다."
    )
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        Page<Product> allProductsPaged = service.getAllProducts(pageNumber-1, pageSize);
        return new ResponseEntity<>(allProductsPaged.map(ProductResponseDto::toProductResponseDto), HttpStatus.OK);
    }

    /**
     * 상품 조회 - 한 개
     * @param id 조회할 상품의 ID
     * @return 조회한 product
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "상품 조회(개별)",
            description = "주어진 ID에 해당하는 상품 하나를 조회하는 API입니다."
    )
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable(value = "id") Long id) {
        Product product = service.getProductById(id);
        ProductResponseDto productResponseDto = ProductResponseDto.toProductResponseDto(product);

        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
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
    public ResponseEntity<Void> updateProduct(@Valid @RequestBody ProductUpdateDto productUpdateDto) {
        service.updateProduct(productUpdateDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 삭제
     * @param id 삭제할 상품의 ID
     * @return HTTP State - No Content
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "상품 삭제",
            description = "주어진 ID에 해당하는 상품을 삭제하는 API입니다."
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
