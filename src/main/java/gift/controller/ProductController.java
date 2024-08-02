package gift.controller;

import gift.DTO.ProductDTO;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 API", description = "상품 생성, 조회, 수정, 삭제를 수행하는 API입니다.")
public class ProductController {

    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 새 상품을 생성하고 맵에 저장함
     *
     * @param productDTO 저장할 상품 객체
     * @return 생성된 상품 정보
     */
    @Operation(summary = "새 상품 생성", description = "새로운 상품을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 상품 생성 실패")
    })
    @PostMapping
    public ProductDTO createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    /**
     * 주어진 ID에 해당하는 상품을 반환함
     *
     * @param id 조회할 상품의 ID
     * @return 조회된 상품 정보
     */
    @Operation(summary = "상품 조회", description = "ID에 해당하는 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 모든 상품을 반환함
     *
     * @return 모든 상품 정보
     */
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    })
    @GetMapping("/all")
    public List<ProductDTO> getProducts() {
        return productService.getAllProducts();
    }

    /**
     * 페이징과 정렬을 사용하여 상품 목록을 가져옵니다.
     *
     * @param page      페이지 번호, 기본값은 0
     * @param size      페이지 크기, 기본값은 10
     * @param criteria  정렬 기준, 기본값은 createdAt
     * @param direction 정렬 방향, 기본값은 desc
     * @return ProductDTO 목록을 포함한 ResponseEntity
     */
    @Operation(summary = "상품 목록 조회", description = "페이지네이션과 정렬을 사용하여 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProduct(
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria,
            @RequestParam(required = false, defaultValue = "desc", value = "direction") String direction) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), criteria));
        List<ProductDTO> productIds = productService.getAllProducts(pageable);
        return ResponseEntity.ok(productIds);
    }

    /**
     * 주어진 ID에 해당하는 상품을 삭제함
     *
     * @param id 삭제할 상품의 ID
     */
    @Operation(summary = "상품 삭제", description = "ID에 해당하는 상품을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    /**
     * 주어진 ID에 해당하는 상품을 갱신함
     *
     * @param id         갱신할 상품의 ID
     * @param productDTO 갱신할 상품 객체
     * @return 갱신된 상품 정보
     */
    @Operation(summary = "상품 갱신", description = "ID에 해당하는 상품을 갱신합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 갱신 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id,
                                    @Valid @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }
}
