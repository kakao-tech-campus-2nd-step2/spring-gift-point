package gift.api;

import gift.dto.OrderDTO;
import gift.dto.PageRequestDTO;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;
    private final UserService  userService;

    public OrderController(OrderService orderService, KakaoService kakaoService, UserService userService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
        this.userService = userService;
    }

    @PostMapping("/api/orders")
    @Operation(summary = "Create order", description = "This API creates a new order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order created and message sent."),
        @ApiResponse(responseCode = "400", description = "Invalid product quantity."),
        @ApiResponse(responseCode = "500", description = "Order created but failed to send message.")
    })
    public ResponseEntity<OrderResponse> createOrder(
        @RequestHeader("Authorization") String authorization,
        @RequestBody OrderDTO orderDTO) {


        OrderResponse orderResponse = orderService.createOrder(authorization, orderDTO);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/api/orders")
    @Operation(summary = "Get order list", description = "This API retrieves the order list with pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders."),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<Page<OrderDTO>> getOrderList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
        @RequestParam(defaultValue = "orderDateTime") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {

        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, size, sortBy, direction);
        Page<OrderDTO> orderPage = orderService.getOrderList(pageRequestDTO);

        return ResponseEntity.ok(orderPage);
    }

    @GetMapping("/api/points")
    @Operation(summary = "Get user points", description = "This API returns the points of the authenticated user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Points retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<Map<String, Object>> getUserPoints(@RequestHeader("Authorization") String authorization) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Authorization 헤더에서 "Bearer " 제거하여 토큰 추출
            String token = authorization.replace("Bearer ", "");

            // 토큰을 사용하여 이메일을 가져옴
            String email = kakaoService.getUserEmail(token);

            // 이메일이 유효한지 확인
            if (email == null) {
                response.put("error", "Unauthorized - Invalid or missing token.");
                return ResponseEntity.status(401).body(response);
            }

            // 이메일을 사용하여 포인트 조회
            int points = userService.getUserPointsByEmail(email);
            response.put("points", points);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal server error.");
            return ResponseEntity.status(500).body(response);
        }
    }
}