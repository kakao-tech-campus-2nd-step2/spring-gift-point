package gift.controller;

import gift.auth.Token;
import gift.dto.KakaoMessageRequestDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.auth.JwtUtil;
import gift.service.KakaoApiService;
import gift.service.OrderService;
import gift.vo.Member;
import gift.vo.Option;
import gift.vo.Order;
import gift.vo.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "주문 관리", description = "주문 처리 및 관련된 API들을 제공합니다.")
public class OrderController {

    private final OrderService orderService;
    private final KakaoApiService kakaoApiService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, KakaoApiService kakaoApiService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.kakaoApiService = kakaoApiService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping()
    @Operation(
            summary = "주문 처리",
            description = "주문을 처리하고, 카카오로 로그인한 경우 주문 내역을 카카오 메시지로 전송하는 API입니다."
    )
    @Parameters({
            @Parameter(name = "orderRequestDto", description = "주문 처리에 필요한 정보가 담긴 DTO", required = true),
            @Parameter(name = "authorizationHeader", description = "인증을 위한 Authorization 헤더", required = true)
    })
    public ResponseEntity<OrderResponseDto> processOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestHeader("Authorization") String authorizationHeader) {
        Member member = jwtUtil.getMemberFromAuthorizationHeader(authorizationHeader);

        Order savedOrder = orderService.createOrder(member.getId(), orderRequestDto);

        Token token = jwtUtil.getBearerTokenFromAuthorizationHeader(authorizationHeader);

        if (jwtUtil.isNotJwtToken(token)) {
            Option option = orderService.getOptionByOptionId(savedOrder.getOptionId());
            Product product = option.getProduct();
            KakaoMessageRequestDto kakaoMessageRequestDto = KakaoMessageRequestDto.toKakaoMessageRequestDto(savedOrder, product.getName(), option.getName());
            kakaoApiService.sendKakaoMessage(token, kakaoMessageRequestDto);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponseDto.toOrderResponseDto(savedOrder));
    }

}
