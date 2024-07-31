package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.dto.OrderPageResponseDto;
import gift.entity.User;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;
    private static final int DEFAULT_SIZE = 20;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    public ResponseEntity<OrderResponseDto> createOrder(
            @LoginMember User loginUser,
            @RequestHeader("Kakao-Authorization") String kakaoAccessToken,
            @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.createOrder(loginUser, kakaoAccessToken, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "사용자의 주문 목록을 조회합니다.")
    public ResponseEntity<OrderPageResponseDto> getUserOrders(
            @LoginMember User loginUser,
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_SIZE);
        OrderPageResponseDto orderPage = orderService.getUserOrders(loginUser.getId(), pageable);
        return new ResponseEntity<>(orderPage, HttpStatus.OK);
    }
}
