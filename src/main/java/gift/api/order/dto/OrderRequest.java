package gift.api.order.dto;

import gift.api.member.domain.Member;
import gift.api.option.domain.Option;
import gift.api.order.domain.Order;

public record OrderRequest(
    Long optionId,
    Integer quantity,
    String message
) {

    public Order toEntity(Member member, Option option) {
        return new Order(member, option, message);
    }
}
