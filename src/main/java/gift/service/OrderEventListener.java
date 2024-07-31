package gift.service;

import gift.domain.Member;
import gift.domain.OrderEvent;
import gift.dto.OrderRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        try {
            OrderRequestDto orderRequestDto = event.getOrderRequestDto();
            Member member = event.getMember();

            // 위시리스트에서 상품 삭제
            wishService.deleteProductFromWishList(member.getId(), orderRequestDto.productId());

            // 주문 메시지 발송
            orderService.sendOrderMessage(orderRequestDto, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}
