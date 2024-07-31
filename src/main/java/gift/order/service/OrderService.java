package gift.order.service;

import gift.kakao.login.dto.KakaoMessageSendResponse;
import gift.kakao.login.service.KakaoLoginService;
import gift.product.option.domain.Option;
import gift.product.option.repository.OptionRepository;
import gift.order.domain.Order;
import gift.order.domain.OrderRequest;
import gift.order.domain.OrderResponse;
import gift.order.repository.OrderRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;

    private final KakaoLoginService kakaoLoginService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        KakaoLoginService kakaoLoginService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoLoginService = kakaoLoginService;
    }

    public OrderResponse createOrder(OrderRequest orderRequest){
        Order order = save(convertRequestToEntity(orderRequest));
        return convertEntityToResponse(order);
    }
    public Order save(Order order){
        return orderRepository.save(order);
    }

    public KakaoMessageSendResponse sendMessage(String accessToken, String message){
        return kakaoLoginService.sendMessage(accessToken, message);
    }
    public Page<OrderResponse> getOrderResponses(Pageable pageable) {
        return orderRepository.findAll(pageable)
            .map(this::convertEntityToResponse);
    }
    private Order convertRequestToEntity(OrderRequest orderRequest){
        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new IllegalArgumentException("OrderService: OptionId가 없다."));
        String orderDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        return new Order(orderRequest.quantity(), orderDateTime, orderRequest.message(), option);
    }
    private OrderResponse convertEntityToResponse(Order order){
        return new OrderResponse(order.getId(),
                order.getOption().getId(),
                        order.getQuantity(),
                        order.getOrderDateTime(),
                        order.getMessage());
    }

}
