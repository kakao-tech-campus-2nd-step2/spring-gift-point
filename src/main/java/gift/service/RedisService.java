package gift.service;

import gift.common.annotation.RedissonLock;
import gift.controller.dto.request.OrderRequest;
import gift.controller.dto.response.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final OrderService orderService;

    public RedisService(OrderService orderService) {
        this.orderService = orderService;
    }

    @RedissonLock(value = "#orderRequest.productId + ':' + #orderRequest.optionId")
    public OrderResponse createOrderRedisLock(Long memberId, OrderRequest orderRequest) {
        return orderService.createOrder(memberId, orderRequest);
    }
}
