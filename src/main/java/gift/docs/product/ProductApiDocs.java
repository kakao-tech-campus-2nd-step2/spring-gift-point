package gift.docs.product;

import gift.product.presentation.dto.OptionRequest;
import gift.product.presentation.dto.ProductRequest;
import gift.product.presentation.dto.ProductResponse;
import gift.product.presentation.dto.ProductResponse.WithOptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Product, Product Option", description = "상품과 상품 옵션 관련 API")
public interface ProductApiDocs {

    @Operation(summary = "상품(옵션 포함) 상세 조회")
    public ResponseEntity<WithOptions> getProduct(Long id);

    @Operation(summary = "상품 목록 페이지로 조회")
    public ResponseEntity<ProductResponse.PagingInfo> getProductsByPage(
        Pageable pageable,
        @Parameter(hidden = true) Integer size,
        Long categoryId
    );

    @Operation(summary = "상품 생성")
    public ResponseEntity<Long> createProduct(
        ProductRequest.Create productRequest);

    @Operation(summary = "상품 수정")
    public ResponseEntity<Long> updateProduct(
        ProductRequest.Update productRequest,
        Long id);

    @Operation(summary = "상품 단일 삭제")
    public ResponseEntity<Long> deleteProduct(Long id);

    @Operation(summary = "상품 id 리스트로 다중 삭제")
    public ResponseEntity<Void> deleteProducts(ProductRequest.Ids ids);

    @Operation(summary = "상품 옵션 다중 추가")
    public ResponseEntity<Void> addOptions(
        List<OptionRequest.Create> optionRequests,
        Long productId);

    @Operation(summary = "상품 옵션 단일 추가")
    public ResponseEntity<Void> addOption(
        OptionRequest.Create optionRequestCreate,
        Long productId);

    @Operation(summary = "상품 옵션 수정")
    public ResponseEntity<Void> updateOption(
        OptionRequest.Update optionRequestUpdate,
        Long productId,
        Long optionId);

    @Operation(summary = "상품 옵션 삭제")
    public ResponseEntity<List<Long>> deleteOption(
        List<Long> optionIds,
        Long productId);

}
