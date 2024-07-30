package gift.controller.api;

import gift.dto.giftorder.GiftOrderRequest;
import gift.dto.giftorder.GiftOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 주문 API")
public interface GiftOrderApi {

    @Operation(summary = "새 주문을 생성한다.")
    ResponseEntity<Void> orderOption(Long memberId, GiftOrderRequest giftOrderRequest);

    @Operation(summary = "특정 주문을 조회한다.")
    ResponseEntity<GiftOrderResponse> getOrder(Long id);

    @Operation(summary = "모든 주문을 페이지 단위로 조회한다.")
    ResponseEntity<List<GiftOrderResponse>> getOrders(Long memberId, Pageable pageable);

    @Operation(summary = "특정 주문을 삭제한다.")
    ResponseEntity<Void> deleteOrder(Long id);
}
