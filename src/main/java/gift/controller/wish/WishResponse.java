package gift.controller.wish;

import gift.controller.member.WishMemberResponse;
import gift.controller.product.WishProductResponse;

public record WishResponse(WishMemberResponse member, WishProductResponse product, Long count) {

}