package gift.docs.product;

import gift.product.presentation.dto.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Order", description = "주문 관련 API")
public interface OrderApiDocs {

    @Operation(summary = "주문 생성")
    public ResponseEntity<Long> createOrder(
        @Parameter(hidden = true) Long memberId,
        OrderRequest.Create orderRequestCreate);

}
