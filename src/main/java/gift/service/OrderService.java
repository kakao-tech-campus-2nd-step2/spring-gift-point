package gift.service;

import gift.common.exception.badRequest.OverStockQuantityException;
import gift.common.exception.notFound.OptionNotFoundException;
import gift.common.exception.unauthorized.TokenErrorException;
import gift.common.exception.unauthorized.TokenNotFoundException;
import gift.common.util.JwtUtil;
import gift.dto.KakaoAccessToken;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final OrderMessageService orderMessageService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final KakaoService kakaoService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, OrderMessageService orderMessageService, JwtUtil jwtUtil,
        MemberRepository memberRepository, KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.orderMessageService = orderMessageService;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public OrderResponse addOrder(OrderRequest orderRequest, String token) {
        String email = jwtUtil.extractEmail(token);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(TokenErrorException::new);

        Option option = validateAndUpdateOption(orderRequest);

        Long usedPoints = orderRequest.getPointAmount();
        Long remainingPoints = member.getPoints() - usedPoints;
        member.setPoints(remainingPoints);

        int earnedPoints = calculateEarnedPoints(option.getProduct().getPrice(),
            orderRequest.getQuantity());
        member.setPoints(member.getPoints() + earnedPoints);

        memberRepository.save(member);

        Order order = createOrder(orderRequest, option);
        wishRepository.deleteByOptionId(orderRequest.getOptionId());

        // 새로운 Access Token 발급
        KakaoAccessToken newAccessToken = kakaoService.refreshAccessToken(member.getRefreshToken());
        member.setRefreshToken(newAccessToken.getRefreshToken());
        memberRepository.save(member);

        orderMessageService.sendOrderMessage(order, newAccessToken.getAccessToken());

        return getOrderResponse(order);
    }

    public List<OrderResponse> getOrders(String token) {
        validateToken(token);
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> new OrderResponse(order.getId(), order.getOptionId(),
                order.getQuantity(), order.getOrderDateTime(), order.getMessage()))
            .toList();
    }

    private void validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new TokenNotFoundException();
        }
        String authToken = token.substring(7);
        String email = jwtUtil.extractEmail(authToken);
        memberRepository.findByEmail(email)
            .orElseThrow(TokenErrorException::new);
    }

    private int calculateEarnedPoints(int price, int quantity) {
        return (int) (price * quantity * 0.03);
    }

    private Option validateAndUpdateOption(OrderRequest orderRequest) {
        Option option = optionRepository.findWithId(orderRequest.getOptionId())
            .orElseThrow(OptionNotFoundException::new);

        if (option.getStockQuantity() < orderRequest.getQuantity()) {
            throw new OverStockQuantityException();
        }
        option.subtractQuantity(orderRequest.getQuantity());
        optionRepository.save(option);
        return option;
    }

    private Order createOrder(OrderRequest orderRequest, Option option) {
        Order order = new Order(orderRequest.getOptionId(), orderRequest.getQuantity(),
            orderRequest.getPointAmount(),
            LocalDateTime.now(), orderRequest.getMessage());
        order = orderRepository.save(order);
        return order;
    }

    private OrderResponse getOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOptionId(), order.getQuantity(),
            order.getOrderDateTime(), order.getMessage());
    }

}
