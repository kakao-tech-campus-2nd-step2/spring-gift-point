package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OrderDTO;
import gift.entity.OrderEntity;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OptionService optionService;
    private final OrderRepository orderRepository;
    private final WishListService wishListService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public OrderService(OptionService optionService, OrderRepository orderRepository,
        WishListService wishListService, KakaoUserService kakaoUserService) {
        this.optionService = optionService;
        this.orderRepository = orderRepository;
        this.wishListService = wishListService;
        this.kakaoUserService = kakaoUserService;
    }

    @Transactional
    public void createOrder(OrderDTO order, Long userId, String email, String accessToken) {
        optionService.subtractOptionQuantity(order.getOptionId(), order.getQuantity());

        wishListService.removeOptionFromWishList(userId, order.getOptionId());

        OrderEntity orderEntity = new OrderEntity(
            order.getOptionId(),
            order.getQuantity(),
            LocalDateTime.now().toString(),
            order.getMessage()
        );

        orderRepository.save(orderEntity);

        try {
            kakaoUserService.sendOrderMessage(accessToken, order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 메시지 전송 실패: " + e.getMessage(), e);
        }
    }
}
