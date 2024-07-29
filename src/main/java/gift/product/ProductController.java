package gift.product;

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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "Product API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "상품 조회", description = "모든 상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    public Page<ProductResponseDTO> getProducts(@ParameterObject Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "상품 추가 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 입력 양식입니다.")
    public void addProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        productService.addProduct(productRequestDTO);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "상품 업데이트", description = "상품의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 상품이거나, 입력 양식이 잘못되었습니다.")
    public void updateProduct(
        @PathVariable(value = "id") Long id,
        @RequestBody ProductRequestDTO productRequestDTO
    ) {
        productService.updateProduct(id, productRequestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "상품 삭제 성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 상품입니다.")
    public void deleteProduct(@PathVariable(value = "id") Long id) {
        productService.deleteProduct(id);
    }
}
