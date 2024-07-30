package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import gift.domain.order.entity.OrderItem;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

@Schema(description = "주문 요청 상품 정보")
public record OrderItemRequest(

    @NotNull(message = "상품을 선택해주세요.")
    @Schema(description = "상품 ID")
    Long productId,

    @NotNull(message = "옵션을 선택해주세요.")
    @Schema(description = "옵션 ID")
    Long optionId,

    @Range(min = 1, max = 100000000, message = "옵션 수량은 1 이상 100,000,000 이하입니다.")
    @Schema(description = "주문 수량")
    int quantity
) {
    public OrderItem toOrderItem(Order order, Product product, Option option) {
        return new OrderItem(null, order, product, option, quantity);
    }
}
