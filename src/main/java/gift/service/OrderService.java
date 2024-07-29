package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.constants.Messages;
import gift.domain.Member;
import gift.domain.Order;
import gift.domain.Wish;
import gift.dto.MessageRequestDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.exception.OrderNotFoundException;
import gift.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final MemberService memberService;
    private final OptionService optionService;
    private final WishService wishService;
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderService(MemberService memberService, OptionService optionService,
        WishService wishService, ProductService productService, OrderRepository orderRepository) {
        this.memberService = memberService;
        this.optionService = optionService;
        this.wishService = wishService;
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public OrderResponseDto order(OrderRequestDto orderRequestDto, String memberEmail) {
        Member member = memberService.findByEmail(memberEmail);
        optionService.subtractQuantity(orderRequestDto.getOptionId(),
            orderRequestDto.getQuantity());
        Long productId = optionService.findById(orderRequestDto.getOptionId()).getProduct().getId();
        Wish wish = wishService.findByEmailAndProductId(memberEmail, productId);
        wishService.deleteById(wish.getId());

        Order order = new Order(orderRequestDto.getOptionId(),member.getId() ,orderRequestDto.getQuantity(),orderRequestDto.getMessage());
        orderRepository.save(order);
        return convertToOrderResponseDto(order);
    }
    public List<OrderResponseDto> findByEmail(String memberEmail){
        Member member = memberService.findByEmail(memberEmail);
        List<Order> orders = orderRepository.findByMemberId(member.getId())
            .orElseThrow(() -> new OrderNotFoundException(Messages.NOT_FOUND_ORDER_MESSAGE));
        return orders.stream()
            .map(this::convertToOrderResponseDto)
            .collect(Collectors.toList());
    }

    public String getOauthToken(String memberEmail) {
        Member member = memberService.findByEmail(memberEmail);
        return member.getAccessToken();
    }

    private OrderResponseDto convertToOrderResponseDto(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getOptionId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }
}
