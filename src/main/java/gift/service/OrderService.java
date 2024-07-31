package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final OptionService optionService;
    private final WishService wishService;

    public OrderService(OrderRepository orderRepository, MemberService memberService, OptionService optionService, WishService wishService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.optionService = optionService;
        this.wishService = wishService;
    }

    public OrderResponse createOrder(OrderRequest orderDto, HttpSession session) {
        Member member = memberService.getMemberById(1L); // 임시!
        Option option = optionService.getOption(orderDto.getOption_id());
        Product product = option.getProduct();

        optionService.decreaseQuantity(orderDto.getOption_id(), orderDto.getQuantity());

        wishService.deleteWish(member,product.getId());

        Order order = new Order(member, option, orderDto.getQuantity(), orderDto.getMessage(), product);
        orderRepository.save(order);

        return new OrderResponse(order);
    }
}
