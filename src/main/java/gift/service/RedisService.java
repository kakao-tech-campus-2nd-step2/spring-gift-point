package gift.service;

import gift.common.annotation.RedissonLock;
import gift.controller.dto.request.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final OrderService orderService;

    public RedisService(OrderService orderService) {
        this.orderService = orderService;
    }

    @RedissonLock(value = "#orderRequest.productId + ':' + #orderRequest.optionId")
    public void createOrderRedisLock(Long memberId, OrderRequest orderRequest) {
        orderService.createOrder(memberId, orderRequest);
    }
}
