package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.model.KakaoMember;
import gift.model.Option;
import gift.model.Order;
import gift.repository.KakaoMemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final KakaoMessageService kakaoMessageService;
    private final KakaoMemberRepository kakaoMemberRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, KakaoMessageService kakaoMessageService, KakaoMemberRepository kakaoMemberRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.kakaoMemberRepository = kakaoMemberRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, String email) {
        Option option = optionRepository.findById(orderRequest.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션입니다"));
        option.subtractQuantity(orderRequest.getQuantity());

        Order order = new Order(option, orderRequest.getQuantity(), LocalDateTime.now(), orderRequest.getMessage());
        Order savedOrder = orderRepository.save(order);

        KakaoMember kakaoMember = kakaoMemberRepository.findByUniqueId(email)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다"));

        kakaoMessageService.sendOrderMessage(savedOrder, kakaoMember.getAccessToken());

        return convertToResponse(savedOrder);
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }
}
