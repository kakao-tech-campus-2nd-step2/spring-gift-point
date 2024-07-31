package gift.dto.response;

import gift.domain.Order;
import gift.domain.Product;

import java.time.LocalDateTime;

public record OrderListResponse(String name, Integer price, Integer quantity, String imageUrl,
                                LocalDateTime orderDateTime) {
    public OrderListResponse(Order order, Product product) {
        this(product.getName(), product.getPrice(), order.getQuantity(), product.getImageUrl(), order.getOrderDateTime());
    }
}
