package gift.controller;

import gift.dto.OrderDto;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "OrderController", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @Operation(
          summary = "주문 생성",
          description = "새 주문을 생성합니다.",
          security = {
                  @SecurityRequirement(name = "bearerAuth"),
                  @SecurityRequirement(name = "Kakao-Authorization")
          }
  )
  @PostMapping
  public ResponseEntity<?> createOrder(
          @RequestHeader("Authorization") String authorization,
          @RequestHeader("Kakao-Authorization") String kakaoAuthorization,
          @RequestBody @Valid OrderDto orderDto) {
    try {
      OrderDto createdOrder = orderService.createOrder(orderDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    } catch (Exception e) {
      logger.error("주문 생성 오류", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 생성 오류: " + e.getMessage());
    }
  }

  @Operation(
          summary = "주문 목록 조회",
          description = "주문 목록을 페이지 단위로 조회합니다.",
          security = {
                  @SecurityRequirement(name = "bearerAuth"),
                  @SecurityRequirement(name = "Kakao-Authorization")
          }
  )
  @GetMapping
  public ResponseEntity<?> getOrders(
          @RequestHeader("Authorization") String authorization,
          @RequestHeader("Kakao-Access-Token") String kakaoAccessToken,
          @RequestParam(defaultValue = "0") int page) {
    try {
      Page<OrderDto> orders = orderService.getOrders(PageRequest.of(page, 10));
      return ResponseEntity.ok(orders);
    } catch (Exception e) {
      logger.error("주문 조회 오류", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 조회 오류: " + e.getMessage());
    }
  }
}