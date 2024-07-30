package gift.service;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.entity.Order;
import gift.exception.WishNotFoundException;
import gift.repository.OrderRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoApiService kakaoApiService;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService, KakaoApiService kakaoApiService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.kakaoApiService = kakaoApiService;
    }

    public OrderResponse processOrder(Long memberId, OrderRequest orderRequest) {
        OrderResponse orderResponse = saveOrder(orderRequest);

        optionService.subtractOptionQuantity(orderRequest.optionId(), orderRequest.quantity());

        Long productId = optionService.getProductIdByOptionId(orderRequest);

        try {
            wishService.findAndDeleteProductInWish(memberId, productId);
        } catch (WishNotFoundException e) {
            log.info("위시리스트에 없는 상품입니다");
        }

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
