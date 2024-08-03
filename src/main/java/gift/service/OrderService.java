package gift.service;

import gift.dto.optionDTO.OptionResponseDTO;
import gift.dto.orderDTO.OrderRequestDTO;
import gift.dto.orderDTO.OrderResponseDTO;
import gift.exception.InvalidInputValueException;
import gift.exception.NotFoundException;
import gift.exception.ServerErrorException;
import gift.model.Member;
import gift.model.Option;
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
        OptionResponseDTO optionResponseDTO = optionService.findOptionById(
            orderRequestDTO.optionId());
        if (optionResponseDTO == null) {
            throw new NotFoundException("존재하지 않는 옵션입니다.");
        }
        Member member = memberService.findMemberByEmail(email);
        if (member == null) {
            throw new InvalidInputValueException("유효하지 않은 이메일입니다.");
        }
        if (orderRequestDTO.quantity() <= 0) {
            throw new InvalidInputValueException("잘못된 수량 입력입니다.");
        }
        Option option = optionService.toEntity(optionResponseDTO);
        Long productPrice = Long.parseLong(option.getProduct().getPrice());
        Long totalPrice = productPrice * orderRequestDTO.quantity();
        if (member.getPoints() < totalPrice) {
            throw new InvalidInputValueException("포인트가 부족합니다.");
        }
        memberService.subtractPoints(email, totalPrice);
        optionService.subtractQuantity(option.getId(), orderRequestDTO.quantity());
        Order order = new Order(null, option, orderRequestDTO.quantity(), LocalDateTime.now(),
            orderRequestDTO.message(), member);
        orderRepository.save(order);
        removeFromWishlistByOptionId(option.getId());
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order.getId(),
            order.getOption().getProduct().getId(),
            order.getOption().getId(), order.getOrderDateTime().toString(), order.getQuantity(),
            order.getMessage());
        return orderResponseDTO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeFromWishlistByOptionId(Long optionId) {
        try {
            wishlistService.removeWishlistByOptionId(optionId);
        } catch (Exception e) {
            throw new ServerErrorException("위시리스트에 없거나 위시리스트에서 지워지지 않았습니다.");
        }
    }

}
