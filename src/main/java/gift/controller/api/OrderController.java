package gift.controller.api;

import gift.dto.OrderDTO;
import gift.dto.KakaoUserDTO;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/{wishlistId}")
    @Operation(summary = "주문 생성", description = "위시리스트 ID를 사용하여 주문을 생성합니다.")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable("wishlistId") Long wishlistId, HttpSession session) {
        KakaoUserDTO kakaoUserDTO = (KakaoUserDTO) session.getAttribute("kakaoUserDTO");
        String accessToken = (String) session.getAttribute("accessToken");

        if (kakaoUserDTO == null || accessToken == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        OrderDTO orderDTO = orderService.placeOrder(kakaoUserDTO, wishlistId, accessToken);

        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
