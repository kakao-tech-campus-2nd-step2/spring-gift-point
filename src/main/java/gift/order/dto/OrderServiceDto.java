package gift.order.dto;

import gift.member.domain.Member;
import gift.option.domain.Option;
import gift.order.domain.Order;
import gift.order.domain.OrderCount;
import gift.order.domain.OrderMessage;

public record OrderServiceDto(Long id, OrderCount count, OrderMessage message, Long memberId, Long optionId) {
    public Order toOrder(Member member, Option option) {
        return new Order(id, count, message, member, option);
    }
}
