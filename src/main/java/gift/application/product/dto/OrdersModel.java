package gift.application.product.dto;

import gift.model.order.Orders;
import gift.model.product.Option;
import gift.model.product.Product;
import java.time.LocalDate;

public class OrdersModel {

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

        public static Info from(Orders orders, Product product, Option option) {
            return new Info(orders.getId(), product.getId(), product.getName(),
                product.getImageUrl(),
                option.getId(), option.getName(), product.getPrice(), orders.getTotalPrice(),
                orders.getQuantity(), orders.getCreatedAt().toLocalDate());
        }
    }
}
