package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Orders;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishService wishService;
    private final KakaoService kakaoService;
    private final KakaoTokenService kakaoTokenService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, WishService wishService,KakaoService kakaoService, KakaoTokenService kakaoTokenService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishService = wishService;
        this.kakaoService = kakaoService;
        this.kakaoTokenService = kakaoTokenService;
    }

    @Transactional
    public OrderResponseDTO createOrder(Member member, OrderRequestDTO orderRequest) {
        Option option = optionRepository.findById(orderRequest.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));

        if (option.getQuantity() < orderRequest.getQuantity()) {
            throw new IllegalArgumentException("Insufficient option quantity");
        }

        option.subtract(orderRequest.getQuantity());
        optionRepository.save(option);

        Orders order = new Orders(member, option, orderRequest.getQuantity(), LocalDateTime.now(), orderRequest.getMessage());
        orderRepository.save(order);

        wishService.removeWish(member.getEmail(), option.getProduct().getId());

        if (member.getEmail() != null) {
            String kakaoAccessToken = kakaoTokenService.getTokenByEmail(member.getEmail());
            kakaoService.sendToMe(kakaoAccessToken, orderRequest.getMessage());
        }

        return new OrderResponseDTO(order.getId(), option.getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }
}