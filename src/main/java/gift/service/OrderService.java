package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
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
    private final WishRepository wishRepository;
    private final KakaoMessageService kakaoMessageService;
    private final MemberService memberService;
    private final TokenService tokenService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, WishRepository wishRepository, KakaoMessageService kakaoMessageService, MemberService memberService, TokenService tokenService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String token) {
        Option option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        // 상품 옵션 수량 차감
        option.subtractQuantity(request.getQuantity());
        optionRepository.save(option);

        // 주문내역 만들기
        Order order = new Order(option, request.getQuantity(), LocalDateTime.now(), request.getMessage());
        orderRepository.save(order);

        // 위시리스트에서 해당 상품 삭제
        wishRepository.deleteByOptionId(request.getOptionId());

        OrderResponse orderResponse = new OrderResponse(
                order.getId(),
                option.getId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );

        // JWT 토큰인지 카카오 엑세스 토큰인지 확인하기
        if (tokenService.isJwtToken(token)) {
            String email = tokenService.extractEmailFromToken(token);
            // JWT 토큰일 경우 메시지 응답 바디에 포함
            logger.info("Order created for email: {}", email);
        } else {
            // 카카오 엑세스 토큰일 경우 카카오 나에게 메시지 보내기
            kakaoMessageService.sendMessage(orderResponse, token);
        }

        return orderResponse;
    }
}