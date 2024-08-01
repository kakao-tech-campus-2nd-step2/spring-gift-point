package gift.order.dto;

import gift.member.domain.Member;
import gift.option.domain.Option;
import gift.order.domain.Order;
import gift.order.domain.OrderQuantity;
import gift.order.domain.OrderMessage;
import gift.order.domain.OrderTotalPrice;

public record OrderServiceDto(Long id, OrderQuantity quantity, OrderMessage message,
                              Long memberId, Long optionId, Long usingPoint) {
    public Order toOrder(Member member, Option option) {
        return new Order(id, quantity, message, member, option, null);
    }

    public Order toOrder(Member member, Option option, OrderTotalPrice orderTotalPrice) {
        return new Order(id, quantity, message, member, option, orderTotalPrice);
    }
}
