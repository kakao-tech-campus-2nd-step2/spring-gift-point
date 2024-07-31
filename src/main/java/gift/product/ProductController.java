package gift.product;

import gift.product.dto.ProductPaginationResponseDTO;
import gift.product.dto.ProductRequestDTO;
import gift.product.dto.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "Product API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회")
    @ApiResponse(responseCode = "200", description = "정상 조회")
    @ApiResponse(responseCode = "400", description = "상품 조회 실패")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    public ProductResponseDTO getProduct(
        @PathVariable(value = "productId") Long productId
    ) {
        return productService.getProductById(productId);
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회(페이지네이션 적용)", description = "카테고리별 모든 상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "정상")
    @ApiResponse(responseCode = "400", description = "카테고리가 존재하지 않는 경우")
    @ApiResponse(responseCode = "400", description = "요청 양식이 잘못된 경우")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public Page<ProductPaginationResponseDTO> getProducts(
        @ParameterObject Pageable pageable,
        @RequestParam long categoryId
    ) {
        return productService.getAllProducts(pageable, categoryId);
    }

    @Deprecated
    @PostMapping
    public void addProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        productService.addProduct(productRequestDTO);
    }

    @Deprecated
    @PatchMapping("/{id}")
    public void updateProduct(
        @PathVariable(value = "id") Long id,
        @RequestBody ProductRequestDTO productRequestDTO
    ) {
        productService.updateProduct(id, productRequestDTO);
    }

    @Deprecated
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable(value = "id") Long id) {
        productService.deleteProduct(id);
    }
}
