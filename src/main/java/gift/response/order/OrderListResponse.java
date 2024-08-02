package gift.response.order;

import java.util.List;

public record OrderListResponse(
    List<OrderResponse> contents
) {

}
