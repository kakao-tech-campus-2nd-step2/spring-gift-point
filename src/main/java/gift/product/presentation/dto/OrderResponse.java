package gift.product.presentation.dto;

import gift.product.business.dto.OrderOut;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    public record Info (
        Long orderId,
        Long productId,
        String productName,
        String optionName,
        Integer quantity,
        LocalDateTime date,
        Integer price,
        String imageUrl
    ) {
        public static Info from(OrderOut.Info info) {
            return new Info(
                info.orderId(),
                info.productId(),
                info.productName(),
                info.optionName(),
                info.quantity(),
                info.date(),
                info.price(),
                info.imageUrl()
            );
        }


        public static List<Info> of(List<OrderOut.Info> infos) {
            return infos.stream()
                .map(Info::from)
                .toList();
        }
    }

    public record Paging (
        boolean hasNext,
        List<Info> orders
    ) {

        public static Paging from(OrderOut.Paging orderOutPaging) {
            return new Paging(
                orderOutPaging.hasNext(),
                Info.of(orderOutPaging.orderList())
            );
        }
    }


}
