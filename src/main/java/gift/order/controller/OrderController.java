package gift.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.common.util.CommonResponse;
import gift.order.dto.OrderResponse;
import gift.order.dto.OrderRequest;
import gift.order.service.KakaoService;
import gift.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order API")
public class OrderController {
    private final OrderService orderService;
    private final KakaoService kakaoService;
    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @PostMapping()
    @Operation(summary = "주문하기", description = "새 주문을 생성한다.")
    public ResponseEntity<?> requestOrder(
            @Valid OrderRequest orderRequest,
            @RequestHeader("Authorization") @Parameter(hidden = true) String authorizationHeader
    ) throws JsonProcessingException
    {
        // Bearer 접두사를 제거하여 액세스 토큰만 추출
        String accessToken = authorizationHeader.replace("Bearer ", "");

        // validate accesstoken
        if (!kakaoService.validateToken(accessToken)) {
            accessToken = kakaoService.renewToken(accessToken);
        }

        // 1. 주문 요청 및 주문 내역 저장
        OrderResponse orderResponse = orderService.requestOrder(orderRequest);

        // 2. 주문 내역 카카오톡 메시지를 통해 나에게 보내기
        kakaoService.sendKakaoMessage(orderResponse, accessToken);

        return ResponseEntity.ok(new CommonResponse<>(orderResponse, "주문이 완료되었습니다.", true));
    }
}
