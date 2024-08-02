package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.orderDTO.OrderRequestDTO;
import gift.dto.orderDTO.OrderResponseDTO;
import gift.model.Member;
import gift.service.KakaoService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "주문 관리 API", description = "주문 관리를 위한 API")
public class OrderController {


    private final OrderService orderService;
    private final KakaoService kakaoService;

    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @PostMapping
    @Operation(summary = "주문하기", description = "새로운 주문을 생성합니다.")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO,
        @LoginMember Member member) {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO, member.getEmail());

        String accessToken = orderRequestDTO.accessToken();
        try {
            kakaoService.sendKakaoMessage(accessToken, orderResponseDTO);
        } catch (Exception e) {
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

}
