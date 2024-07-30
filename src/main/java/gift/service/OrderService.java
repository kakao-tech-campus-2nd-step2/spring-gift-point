package gift.service;

import gift.dto.OrderDTO;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    private final OptionRepository optionRepository;
    private final WishlistRepository wishlistRepository;
    private final OrderRepository orderRepository;
    private final KakaoService kakaoService;

    @Autowired
    public OrderService(OptionRepository optionRepository, WishlistRepository wishlistRepository, OrderRepository orderRepository, KakaoService kakaoService) {
        this.optionRepository = optionRepository;
        this.wishlistRepository = wishlistRepository;
        this.orderRepository = orderRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO, String userEmail) {
        try {
            LOGGER.info("옵션 ID로 옵션 정보 가져오기: " + orderDTO.getOptionId());
            Option option = optionRepository.findById(orderDTO.getOptionId())
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 옵션 ID입니다: " + orderDTO.getOptionId()));

            LOGGER.info("가져온 옵션: " + option.getName() + " 수량: " + option.getQuantity());

            option.subtractQuantity(orderDTO.getQuantity());
            LOGGER.info("업데이트된 옵션 수량: " + option.getQuantity());
            optionRepository.save(option);

            LOGGER.info("사용자의 위시리스트에서 옵션 삭제: " + userEmail);
            wishlistRepository.deleteByUserEmailAndProductId(userEmail, option.getProduct().getId());

            String message = orderDTO.getMessage() + "\n옵션: " + option.getName() + "\n수량: " + orderDTO.getQuantity();
            LOGGER.info("사용자에게 메시지 전송: " + userEmail);
            kakaoService.sendMessageToMe(userEmail, message);

            Order order = new Order(orderDTO.getOptionId(), orderDTO.getQuantity(), LocalDateTime.now(), userEmail, orderDTO.getMessage());
            orderRepository.save(order);

            orderDTO.setId(order.getId());
            orderDTO.setOrderDateTime(order.getOrderDateTime());

            LOGGER.info("주문 생성 완료: " + orderDTO.getId());

            return orderDTO;
        } catch (Exception e) {
            LOGGER.severe("주문 생성 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("주문 생성 실패", e);
        }
    }
}