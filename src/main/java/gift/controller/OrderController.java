package gift.controller;

import gift.dto.memberDto.MemberDto;
import gift.dto.orderDto.OrderRequestDto;
import gift.dto.orderDto.OrderResponseDto;
import gift.model.member.LoginMember;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RestController
@Tag(name = "Order Management", description = "Order Management API")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "새로운 주문 요청", description = "새로운 주문을 요청할 때 사용하는 API")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestHeader("Kakao-Authorization") String kakaoAuthorization,
            @RequestBody OrderRequestDto orderRequestDto,
            @LoginMember MemberDto memberDto){

        OrderResponseDto orderResponseDto = orderService.requestOrder(orderRequestDto, kakaoAuthorization, memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrderList(
            @RequestParam(defaultValue = "0") int page,
            @LoginMember MemberDto memberDto){
        Page<OrderResponseDto> orderListResponseDtos = orderService.getOrderList(memberDto, page);
        return ResponseEntity.ok().body(orderListResponseDtos);
    }
}
