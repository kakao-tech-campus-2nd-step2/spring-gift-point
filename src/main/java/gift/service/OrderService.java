package gift.service;

import gift.dto.OrderRequestDTO;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private KakaoMessageService kakaoMessageService;

    public Order placeOrder(String email, OrderRequestDTO orderRequestDTO, String accessToken)
        throws Exception {
        Member member = memberService.findMemberEntityByEmail(email);
        Option option = optionService.findOptionById(orderRequestDTO.getOptionId());
        optionService.subtractOptionQuantity(option.getId(), orderRequestDTO.getQuantity());

        Order order = new Order(member, option, orderRequestDTO.getQuantity(), orderRequestDTO.getMessage());
        Order savedOrder = orderRepository.save(order);

        kakaoMessageService.sendOrderMessage(accessToken, createOrderMessage(savedOrder));
        return savedOrder;
    }

    private String createOrderMessage(Order order) {
        return "주문이 완료되었습니다.\n" +
            "상품명: " + order.getOption().getProduct().getName() + "\n" +
            "옵션: " + order.getOption().getName() + "\n" +
            "수량: " + order.getQuantity() + "\n" +
            "메시지: " + order.getMessage();
    }
}
