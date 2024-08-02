package gift.service;

import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OrderRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OptionService optionService;

    public Order placeOrder(String email, OrderRequestDTO orderRequestDTO) {
        Member member = memberService.findMemberEntityByEmail(email);
        Option option = optionService.findOptionById(orderRequestDTO.getOptionId());
        optionService.subtractOptionQuantity(option.getId(), orderRequestDTO.getQuantity());

        Order order = new Order(member, option, orderRequestDTO.getQuantity(), orderRequestDTO.getMessage());
        return orderRepository.save(order);
    }

    public Page<OrderResponseDTO> getOrderList(String token, PageRequest pageRequest) {
        String email = JwtUtil.extractEmail(JwtUtil.getBearerToken(token));
        Member member = memberService.findMemberEntityByEmail(email);
        Page<Order> orders = orderRepository.findByMember(member, pageRequest);
        return orders.map(OrderResponseDTO::fromEntity);
    }
}
