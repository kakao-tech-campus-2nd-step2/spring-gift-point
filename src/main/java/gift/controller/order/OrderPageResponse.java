package gift.controller.order;

import gift.controller.response.PageInfo;
import java.util.List;

public record OrderPageResponse(PageInfo pageInfo, List<OrderResponse> orderResponse) {

}
