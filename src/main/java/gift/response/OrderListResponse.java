package gift.response;

import java.util.List;

public record OrderListResponse(
    List<OrderResponse> contents
) {

}
