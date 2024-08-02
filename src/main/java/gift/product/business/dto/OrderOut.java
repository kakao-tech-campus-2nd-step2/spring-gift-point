package gift.product.business.dto;

import gift.product.persistence.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public class OrderOut {

    public record Info(
        Long orderId,
        Long productId,
        String productName,
        String optionName,
        Integer quantity,
        LocalDateTime date,
        Integer price,
        String imageUrl
    ) {

            public static Info from(Order order) {
                return new Info(
                    order.getId(),
                    order.getProduct().getId(),
                    order.getProduct().getName(),
                    order.getProduct().getOptionByOptionId(order.getOptionId()).getName(),
                    order.getQuantity(),
                    order.getCreatedDate(),
                    order.getProduct().getPrice(),
                    order.getProduct().getUrl()
                );
            }

            public static List<Info> of(List<Order> content) {
                return content.stream()
                    .map(Info::from)
                    .toList();
            }

    }

    public record Paging(
        boolean hasNext,
        List<Info> orderList
    ) {

        public static Paging from(Page<Order> orderPage) {
            return new Paging(
                orderPage.hasNext(),
                Info.of(orderPage.getContent())
            );
        }
    }


}
