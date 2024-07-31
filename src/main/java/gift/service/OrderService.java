package gift.service;

import gift.config.JwtConfig;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final OptionService optionService;
    private final WishService wishService;
    private final JwtConfig jwtConfig;

    public OrderService(OrderRepository orderRepository, MemberService memberService, OptionService optionService, WishService wishService, JwtConfig jwtConfig) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.optionService = optionService;
        this.wishService = wishService;
        this.jwtConfig = jwtConfig;
    }

    public OrderResponse createOrder(OrderRequest orderDto, String access_token) {
        String email = jwtConfig.extractEmail(access_token);
        Member member = memberService.getMemberbyEmail(email);
        Option option = optionService.getOption(orderDto.getOption_id());
        Product product = option.getProduct();

        optionService.decreaseQuantity(orderDto.getOption_id(), orderDto.getQuantity());

        wishService.deleteWish(access_token, product.getId());

        Order order = new Order(member, option, orderDto.getQuantity(), orderDto.getMessage(), product);
        orderRepository.save(order);

        return new OrderResponse(order);
    }
}
