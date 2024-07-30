package gift.domain;

import gift.domain.Member;
import gift.dto.OrderRequestDto;

public class OrderEvent {
    private final OrderRequestDto orderRequestDto;
    private final Member member;

    public OrderEvent(OrderRequestDto orderRequestDto, Member member) {
        this.orderRequestDto = orderRequestDto;
        this.member = member;
    }

    public OrderRequestDto getOrderRequestDto() {
        return orderRequestDto;
    }

    public Member getMember() {
        return member;
    }
}
