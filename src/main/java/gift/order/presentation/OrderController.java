package gift.order.presentation;

import gift.exception.NotFoundMember;
import gift.member.presentation.MemberController.LoginMember;
import gift.member.persistence.Member;
import gift.member.application.dto.RegisterRequestDto;
import gift.order.application.dto.OrderRequestDto;
import gift.order.application.dto.OrderResponseDto;
import gift.order.application.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
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
    public OrderResponseDto createdOrder(@LoginMember Member member, @RequestBody OrderRequestDto orderRequestDto)
        throws NotFoundMember {
        return orderService.createOrder(member, orderRequestDto);
    }

    @GetMapping
    public Page<OrderResponseDto> getOrders(@LoginMember Member member, Pageable pageable)
        throws NotFoundMember {
        return orderService.getOrders(member, pageable);
    }

    @DeleteMapping("/{orderId}")
    public void deletOrder(@LoginMember RegisterRequestDto memberRequestDto, @PathVariable Long orderId)
        throws NotFoundMember {
        orderService.deleteOrder(memberRequestDto, orderId);
    }

}
