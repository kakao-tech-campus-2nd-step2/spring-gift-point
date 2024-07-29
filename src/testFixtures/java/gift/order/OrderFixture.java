package gift.order;

import gift.member.entity.Member;
import gift.option.entity.Option;
import gift.order.dto.OrderReqDto;
import gift.order.entity.Order;

public class OrderFixture {

    public static Order createOrder(Member member, Option option, Integer quantity, String message) {
        return new Order(member, option, quantity, message);
    }

    public static OrderReqDto createOrderReqDto(Long optionId, Integer quantity, String message) {
        return new OrderReqDto(optionId, quantity, message);
    }
}
