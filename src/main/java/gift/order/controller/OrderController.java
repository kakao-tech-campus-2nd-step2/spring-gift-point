package gift.order.controller;

import gift.common.auth.JwtUtil;
import gift.common.util.CommonResponse;
import gift.order.dto.OrderResponse;
import gift.order.dto.OrderRequest;
import gift.order.service.KakaoService;
import gift.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "주문하기 API", description = "주문하기 및 주문 목록 조회하는 API")
public class OrderController {
    private final OrderService orderService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;
    public OrderController(OrderService orderService, KakaoService kakaoService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
        this.jwtUtil = jwtUtil;
    }

    // 1. 주문하기
    @PostMapping()
    @Operation(summary = "주문하기", description = "새 주문을 생성한다.")
    public ResponseEntity<?> requestOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader
    ) 
    {
        // 토큰 추출
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : null;

        if (token == null || !jwtUtil.isTokenValid(token)) {
            // 401 Unauthorized
            return ResponseEntity.status(401).body(new CommonResponse<>(null, "Invalid or missing token", false));
        }

        // 1. 주문 요청 및 주문 내역 저장
        OrderResponse orderResponse = orderService.requestOrder(orderRequest);

        // 2. 주문 내역 카카오톡 메시지를 통해 나에게 보내기
        //kakaoService.sendKakaoMessage(orderResponse, accessToken);

        return ResponseEntity.status(201).body(new CommonResponse<>(orderResponse, "주문이 완료되었습니다.", true));
    }

    // 2. 주문 목록 조회 (페이지네이션 적용)
    @GetMapping()
    @Operation(summary = "주문 목록 조회 (페이지네이션 적용)", description = "회원의 주문 목록을 페이지 단위로 조회한다.")
    public ResponseEntity<?> getOrderByPagination(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "price,desc") String sort) {
        // 토큰 추출
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : null;

        if (token == null || !jwtUtil.isTokenValid(token)) {
            // 401 Unauthorized
            return ResponseEntity.status(401).body(new CommonResponse<>(null, "Invalid or missing token", false));
        }

        // sort 파라미터를 ',' 기준으로 분리
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        String direction = sortParams.length > 1 ? sortParams[1] : "desc";

        // 페이지네이션 처리
        Page<OrderResponse> orderResponseList = orderService.getOrderByPage(page, size, sortBy, direction);

        return ResponseEntity.ok(new CommonResponse<>(orderResponseList, "주문 목록 조회 성공", true));
    }

}
