package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.Member;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.dto.PageRequestDto;
import gift.dto.WishResponseDto;
import gift.service.OrderService;
import gift.service.ProductService;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "주문 API")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문하기", description = "새 주문을 생성한다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto, @Parameter(hidden = true)@LoginMember Member member) {
        OrderResponseDto orderResponseDto = orderService.processOrder(orderRequestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회 (페이지네이션 적용)", description = "주문 목록을 페이지 단위로 조회한다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Page<OrderResponseDto>> getOrder(
        @Parameter(hidden = true) @LoginMember Member member,
        @Valid @ModelAttribute PageRequestDto pageRequestDto) {

        Pageable pageable = pageRequestDto.toPageable();
        Page<OrderResponseDto> orders = orderService.findByMemberId(member.getId(), pageable);
        return ResponseEntity.ok(orders);
    }
}
