package gift.controller.wish;

import gift.controller.response.PageInfo;
import java.util.List;

public record WishPageResponse(PageInfo pageInfo, List<WishResponse> wishDtoList) {

}