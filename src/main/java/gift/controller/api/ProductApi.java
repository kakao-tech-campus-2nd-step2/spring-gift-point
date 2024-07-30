package gift.controller.api;

import gift.dto.product.ProductRequest;
import gift.dto.product.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 API")
public interface ProductApi {

    @Operation(summary = "새 상품을 등록한다.")
    ResponseEntity<Void> addProduct(ProductRequest productRequest, String memberRole);

    @Operation(summary = "기존 상품을 수정한다.")
    ResponseEntity<Void> updateProduct(Long id, ProductRequest productRequest);

    @Operation(summary = "특정 상품을 조회한다.")
    ResponseEntity<ProductResponse> getProduct(Long id);

    @Operation(summary = "모든 상품을 페이지 단위로 조회한다.")
    ResponseEntity<List<ProductResponse>> getProducts(Pageable pageable);

    @Operation(summary = "특정 상품을 삭제한다.")
    ResponseEntity<Void> deleteProduct(Long id);
}
