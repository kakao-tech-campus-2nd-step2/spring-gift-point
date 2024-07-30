package gift.order.restapi.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.PagedDto;
import gift.core.domain.order.Order;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagedOrderResponse(
        List<OrderResponse> contents
) {
    public static PagedOrderResponse from(PagedDto<Order> pagedDto) {
        return new PagedOrderResponse(
                pagedDto.contents().stream()
                        .map(OrderResponse::of)
                        .toList()
        );
    }
}
