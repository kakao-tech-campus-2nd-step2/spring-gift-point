package gift.event;

import gift.service.OrderService;
import gift.service.WishService;
import gift.service.dto.OrderDto;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventHandler {
    private final WishService wishService;
    private final OrderService orderService;

    public OrderEventHandler(WishService wishService, OrderService orderService) {
        this.wishService = wishService;
        this.orderService = orderService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteWishListener(OrderEventDto orderEventDto) {
        wishService.deleteIfExists(orderEventDto.productId(), orderEventDto.memberId());
    }

    @Async
    @EventListener
    public void sendKakaoMessageListener(OrderEventDto orderEventDto) {
        orderService.sendKakaoMessage(OrderDto.from(orderEventDto));
    }
}
