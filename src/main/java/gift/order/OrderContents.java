package gift.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OrderContents(
    List<OrderResponse> contents
) {
    public static OrderContents from(List<OrderInfo> orderInfos) {
        List<OrderResponse> orderResponseList = orderInfos.stream()
            .map(OrderResponse::from)
            .toList();

        return new OrderContents(
            orderResponseList
        );
    }
}
