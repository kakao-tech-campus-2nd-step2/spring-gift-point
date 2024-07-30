package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "주문 관리", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	private final OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@Operation(summary = "주문 생성", description = "새 주문을 생성합니다.")
	@ApiResponse(responseCode = "201", description = "주문 생성 성공")
	@PostMapping
	public ResponseEntity<OrderResponse> createOrdeer(@RequestHeader("Authorization") String token,
			@Valid @RequestBody OrderRequest request, BindingResult bindingResult){
		OrderResponse response = orderService.createOrder(token, request, bindingResult);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
