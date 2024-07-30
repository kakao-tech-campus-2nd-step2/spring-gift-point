package gift.controller.product.dto;

import gift.application.product.dto.OrdersModel;
import java.time.LocalDate;

public class OrdersResponse {

    public record Info(
        Long orderId,
        Long productId,
        String productName,
        String productImageUrl,
        Long optionId,
        String optionName,
        Integer price,
        Integer totalPrice,
        Integer count,
        LocalDate createdAt
    ) {

        public static Info from(OrdersModel.Info info) {
            return new Info(info.orderId(), info.productId(), info.productName(),
                info.productImageUrl(),
                info.optionId(), info.optionName(), info.price(), info.totalPrice(), info.count(),
                info.createdAt());
        }
    }
}
