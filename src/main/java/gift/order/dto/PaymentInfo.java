package gift.order.dto;

import gift.order.dto.request.OrderRequest;
import gift.product.option.entity.Option;

public record PaymentInfo(
    Long optionId,
    Integer quantity,
    String message,
    Integer price,
    Boolean usePoint,
    Integer usedPoint
) {

    public static PaymentInfo of(Option option, OrderRequest orderRequest) {
        return new PaymentInfo(
            option.getId(),
            orderRequest.quantity(),
            orderRequest.message(),
            option.getPrice(),
            orderRequest.usePoint(),
            orderRequest.point()
        );
    }

}
