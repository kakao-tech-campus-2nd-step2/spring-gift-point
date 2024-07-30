package gift.controller.product;

import gift.controller.response.PageInfo;
import java.util.List;

public record ProductPageResponse(PageInfo pageInfo, List<ProductResponse> productResponses) {

}
