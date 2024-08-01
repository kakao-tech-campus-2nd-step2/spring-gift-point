package gift.event;

import gift.service.OrderService;
import gift.service.WishService;
import gift.service.dto.OrderDto;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {
    private final WishService wishService;
    private final OrderService orderService;

    public OrderEventHandler(WishService wishService, OrderService orderService) {
        this.wishService = wishService;
        this.orderService = orderService;
    }

    @Async
    @EventListener
    public void deleteWishListener(OrderEventDto orderEventDto) {
        wishService.deleteIfExists(orderEventDto.productId(), orderEventDto.memberId());
    }

    @Async
    @EventListener
    public void sendKakaoMessageListener(OrderEventDto orderEventDto) {
        orderService.sendKakaoMessage(OrderDto.from(orderEventDto));
    }
}
