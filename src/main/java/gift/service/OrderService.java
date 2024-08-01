package gift.service;

import gift.dto.OptionResponseDTO;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.model.Option;
import gift.model.Member;
import gift.model.Order;
import gift.repository.OrderRepository;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {


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
        OptionResponseDTO optionResponseDTO = optionService.findOptionById(orderRequestDTO.optionId());
        Member member = memberService.findMemberByEmail(email);
        Option option = optionService.toEntity(optionResponseDTO);
        optionService.subtractQuantity(option.getId(), orderRequestDTO.quantity());
        Order order = new Order(null, option, orderRequestDTO.quantity(), LocalDateTime.now(),
            orderRequestDTO.message(), member);
        orderRepository.save(order);
        removeFromWishlistByOptionId(option.getId());
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order.getId(), order.getOption().getProduct().getId(),
            order.getOption().getId(),  order.getOrderDateTime().toString(), order.getQuantity(),
            order.getMessage());
        return orderResponseDTO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeFromWishlistByOptionId(Long optionId) {
        try {
            wishlistService.removeWishlistByOptionId(optionId);
        } catch (Exception e) {

        }
    }

}
