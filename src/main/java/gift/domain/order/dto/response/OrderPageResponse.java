package gift.domain.order.dto.response;

import java.util.List;

public record OrderPageResponse(
    boolean hasNext,
    List<OrderResponse> options
) {

}
