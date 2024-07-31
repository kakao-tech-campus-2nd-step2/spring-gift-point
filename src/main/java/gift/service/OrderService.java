package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final KakaoMessageService kakaoMessageService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String token, boolean sendKakaoMessage) {
        Option option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID: " + request.getOptionId()));

        if (option.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient option quantity for ID: " + request.getOptionId());
        }

        option.subtractQuantity(request.getQuantity()); // 옵션 수량 차감
        optionRepository.save(option);

        Order order = new Order(option, request.getQuantity(), LocalDateTime.now(), request.getMessage());
        orderRepository.save(order);

        OrderResponse response = new OrderResponse(
                order.getId(),
                option.getId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );

        if (sendKakaoMessage) {
            try {
                kakaoMessageService.sendMessage(response, token);
            } catch (Exception e) {
                logger.error("카카오 로그인 경우에만 메시지를 보낼 수 있습니다.: {}", e.getMessage());
            }
        }

        return response;
    }
}