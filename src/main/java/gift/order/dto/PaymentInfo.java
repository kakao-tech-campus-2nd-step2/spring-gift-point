package gift.order.dto;

import gift.order.dto.request.OrderRequest;
import gift.product.option.entity.Option;

public record PaymentInfo(
    Long optionId,
    Integer quantity,
    String message,
    Integer price,
    Integer totalPrice,
    Boolean usePoint,
    Integer usedPoint,
    Integer discountedPrice,
    Integer payedPrice,
    Integer accumulatedPrice
) {

    public static PaymentInfo of(Option option, OrderRequest request, Integer totalPrice,
        Integer discountedPrice, Integer payedPrice, Integer accumulatedPrice) {
        return new PaymentInfo(
            option.getId(),
            request.quantity(),
            request.message(),
            option.getPrice(),
            totalPrice,
            request.usePoint(),
            request.point(),
            discountedPrice,
            payedPrice,
            accumulatedPrice
        );
    }

}
