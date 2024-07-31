package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Order;
import gift.exception.WishException;
import gift.service.MemberService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    public OrderController(OrderService orderService, MemberService memberService) {
        this.orderService = orderService;
        this.memberService = memberService;
    }

    @Operation(summary = "상품 주문", description = "상품을 주문합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "주문 성공",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))
            )
        }
    )
    @PostMapping
    public ResponseEntity<OrderResponseDto> addOrder(@LoginUser String email, @Valid @RequestBody OrderRequestDto orderRequestDto)
        throws WishException {
        OrderResponseDto orderResponseDto = orderService.addOrder(email, orderRequestDto);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "주문 조회", description = "주문을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(
        @LoginUser String email,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "orderDateTime,desc") String sort) {

        Long memberId = memberService.getMemberId(email);
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Order> orderPage = orderService.findByMemberId(memberId, pageable);
        List<OrderResponseDto> orderList = orderPage.stream()
            .map(order -> new OrderResponseDto(
                order.getId(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()

            ))
            .collect(Collectors.toList());

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }
}
