package gift.controller;

import gift.annotation.KakaoUser;

import gift.dto.order.OrderRequestDTO;
import gift.dto.order.OrderResponseDTO;
import gift.dto.user.KakaoUserDTO;

import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/orders")
@Tag(name = "Kakao Wish API", description = "카카오 위시리스트 및 주문 관련 API")
public class KakaoOrderController {

    private final OrderService orderService;

    public KakaoOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "주문 생성",
            description = "옵션 수량 차감, 위시리스트에서 삭제, 메시지 전송",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    public ResponseEntity<OrderResponseDTO> handleKakaoOrder(@KakaoUser KakaoUserDTO kakaoUserDTO, @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.processOrder(kakaoUserDTO, orderRequestDTO);

        return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
    }

}