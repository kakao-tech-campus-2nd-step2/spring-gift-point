package gift.service;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.entity.Order;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoApiService kakaoApiService;
    private final PointService pointService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService, KakaoApiService kakaoApiService, PointService pointService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.kakaoApiService = kakaoApiService;
        this.pointService = pointService;
    }

    @Transactional
    public OrderResponse processOrder(Long memberId, OrderRequest orderRequest) {
        pointService.subtractPoint(memberId, orderRequest);

        OrderResponse orderResponse = saveOrder(orderRequest);
        optionService.subtractOptionQuantity(orderRequest.optionId(), orderRequest.quantity());

        Long productId = optionService.getProductIdByOptionId(orderRequest);
        wishService.deleteProductInWish(memberId, productId);

        kakaoApiService.sendMessageToMe(memberId, orderRequest);
        return orderResponse;
    }

    private OrderResponse saveOrder(OrderRequest orderRequest) {
        Order order = new Order(orderRequest.optionId(), orderRequest.quantity(), orderRequest.message());
        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOptionId(),
                savedOrder.getQuantity(),
                savedOrder.getOrderDateTime(),
                savedOrder.getMessage()
        );
    }
}
