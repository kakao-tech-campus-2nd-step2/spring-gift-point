package gift.order.controller;

import gift.order.dto.OrderDto;
import gift.order.service.OrderService;
import gift.user.service.KakaoApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Order API", description = "주문 관련 API")
public class OrderController {

  private final OrderService orderService;
  private final KakaoApiService kakaoApiService;

  @Autowired
  public OrderController(OrderService orderService, KakaoApiService kakaoApiService) {
    this.orderService = orderService;
    this.kakaoApiService = kakaoApiService;
  }

  @PostMapping("/orders")
  @Operation(summary = "주문 완료", description = "주문을 완료하고 결과를 반환합니다.")
  public Mono<String> completeOrder(
      @RequestBody @Parameter(description = "주문 데이터", required = true) OrderDto orderDto) {
    return Mono.fromCallable(() -> orderService.createOrder(orderDto))
        .flatMap(order -> Mono.just("주문이 완료되었습니다."))
        .onErrorResume(e -> Mono.just("주문 처리 중 오류가 발생했습니다: " + e.getMessage()));
  }
}