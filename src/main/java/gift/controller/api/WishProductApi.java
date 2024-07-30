package gift.controller.api;

import gift.dto.wishproduct.WishProductAddRequest;
import gift.dto.wishproduct.WishProductResponse;
import gift.dto.wishproduct.WishProductUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "위시리스트 API")
public interface WishProductApi {

    @Operation(summary = "회원의 위시 리스트에 상품을 추가한다.")
    ResponseEntity<Void> addWishProduct(WishProductAddRequest wishProductAddRequest, Long memberId);

    @Operation(summary = "회원의 특정 위시 리스트를 수정한다.")
    ResponseEntity<Void> updateWishProduct(Long id, WishProductUpdateRequest wishProductUpdateRequest);

    @Operation(summary = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
    ResponseEntity<List<WishProductResponse>> getWishProducts(Long memberId, Pageable pageable);

    @Operation(summary = "회원의 위시 리스트에서 상품을 삭제한다.")
    ResponseEntity<Void> deleteWishProduct(Long id);
}
