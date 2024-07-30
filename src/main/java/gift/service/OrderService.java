package gift.service;


import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final MemberService memberService;
    private final WishlistService wishlistService;

    public OrderService(OrderRepository orderRepository, OptionService optionService,
        MemberService memberService,
        WishlistService wishlistService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.memberService = memberService;
        this.wishlistService = wishlistService;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, String email) {
        Option option = optionService.findOptionById(orderRequestDTO.optionId());
        Member member = memberService.findMemberByEmail(email);
        Long quantity = orderRequestDTO.quantity();
        optionService.subtractQuantity(option.getId(), quantity);
        Order order = new Order(null, option, quantity, LocalDateTime.now(),
            orderRequestDTO.message(), member);
        orderRepository.save(order);
        removeFromWishlist(member.getEmail(), option.getProduct().getId());
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order.getId(),
            order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(),
            order.getMessage());
        return orderResponseDTO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeFromWishlist(String email, Long productId) {
        try {
            wishlistService.removeWishlist(email, productId);
        } catch (Exception e) {
            logger.error("주문은 성공했지만 위시리스트에서의 삭제는 실패했습니다.");
        }
    }

}
