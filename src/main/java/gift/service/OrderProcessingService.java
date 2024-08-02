package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.model.Member;
import gift.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    private final OrderService orderService;
    private final WishService wishService;
    private final OptionService optionService;
    private final PointService pointService;

    public OrderProcessingService(OrderService orderService, WishService wishService, OptionService optionService, PointService pointService) {
        this.orderService = orderService;
        this.wishService = wishService;
        this.optionService = optionService;
        this.pointService = pointService;
    }

    @Transactional
    public OrderResponse orderProcess(OrderRequest request, Member member) {
        // 옵션 개수 차감
        optionService.subtractQuantity(request);

        // 해당 상품이 wish list 에 있다면 제거
        wishService.deleteWishByOrder(
                member,
                optionService.getProductById(request)
        );

        // 포인트 사용
        pointService.subtractPoint(request.usedPoint(), member);

        // 주문
        Order order = orderService.make(request);

        // 주문 메시지 보냄
        orderService.sendOrderMessage(request, member);

        return order.toDto();
    }
}
