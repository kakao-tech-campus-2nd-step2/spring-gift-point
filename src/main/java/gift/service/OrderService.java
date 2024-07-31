package gift.service;

import gift.common.exception.badRequest.OverStockQuantityException;
import gift.common.exception.notFound.OptionNotFoundException;
import gift.common.exception.unauthorized.TokenErrorException;
import gift.common.exception.unauthorized.TokenNotFoundException;
import gift.common.util.JwtUtil;
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

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, OrderMessageService orderMessageService, JwtUtil jwtUtil,
        MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.orderMessageService = orderMessageService;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public OrderResponse addOrder(OrderRequest orderRequest, String token) {
        String email = jwtUtil.extractEmail(token);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(TokenErrorException::new);

        Option option = validateAndUpdateOption(orderRequest);
        Order order = createOrder(orderRequest, option);
        wishRepository.deleteByOptionId(orderRequest.getOptionId());
        //orderMessageService.sendOrderMessage(order, token);

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
            LocalDateTime.now(), orderRequest.getMessage());
        order = orderRepository.save(order);
        return order;
    }

    private OrderResponse getOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOptionId(), order.getQuantity(),
            order.getOrderDateTime(), order.getMessage());
    }

}
