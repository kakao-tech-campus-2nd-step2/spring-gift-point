package gift.order;

import gift.exception.NotFoundMember;
import gift.login.LoginMember;
import gift.member.MemberRequestDto;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDto createdOrder(@LoginMember MemberRequestDto memberRequestDto, @RequestBody OrderRequestDto orderRequestDto)
        throws NotFoundMember {
        return orderService.createOrder(memberRequestDto, orderRequestDto);
    }

    @GetMapping
    public List<OrderResponseDto> getOrders(@LoginMember MemberRequestDto memberRequestDto)
        throws NotFoundMember {
        return orderService.getOrders(memberRequestDto);
    }

    @DeleteMapping("/{orderId}")
    public void deletOrder(@LoginMember MemberRequestDto memberRequestDto, @PathVariable Long orderId)
        throws NotFoundMember {
        orderService.deleteOrder(memberRequestDto, orderId);
    }

}
