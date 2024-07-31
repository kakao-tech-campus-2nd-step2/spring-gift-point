package gift.api.order.service;

import gift.api.member.service.KakaoService;
import gift.api.option.service.OptionService;
import gift.api.order.domain.Order;
import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.api.wishlist.service.WishService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderFacade {

    private final OrderService orderService;
    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoService kakaoService;

    public OrderFacade(OrderService orderService, OptionService optionService,
        WishService wishService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.optionService = optionService;
        this.wishService = wishService;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public OrderResponse order(Long memberId, OrderRequest orderRequest) {
        optionService.subtract(orderRequest.optionId(), orderRequest.quantity());
        wishService.delete(memberId,
            optionService.findOptionById(orderRequest.optionId()).getProductId());
        Order order = orderService.saveOrder(
            orderRequest.toEntity(optionService.findOptionById(orderRequest.optionId())));
        kakaoService.sendMessage(memberId, orderService.createBody(orderRequest));
        return OrderResponse.of(order);
    }
}
