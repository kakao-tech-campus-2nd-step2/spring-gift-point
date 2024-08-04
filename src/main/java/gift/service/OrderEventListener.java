package gift.service;

import gift.domain.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventListener {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    private final WishService wishService;
    private final OrderService orderService;

    public OrderEventListener(WishService wishService, OrderService orderService) {
        this.wishService = wishService;
        this.orderService = orderService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(OrderEvent event) {
        try {
            // 위시리스트에서 상품 삭제
            wishService.deleteProductFromWishList(event.getMember().getId(), event.getProductId());
            // 주문 메시지 발송
            orderService.sendOrderMessage(event.getOrderId(), event.getMember());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}
