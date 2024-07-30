package gift.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import gift.controller.OrderController;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;

public class OrderTest {

	@Mock
	private OrderService OrderService;
	
	@InjectMocks
	private OrderController orderController;
	
	@Mock
	private BindingResult bindingResult;
	
	private OrderRequest orderRequest;
	private OrderResponse orderResponse;
	
	LocalDateTime now = LocalDateTime.now();
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		orderRequest = new OrderRequest(1L, 2, "Please handle this order with care.");
		orderResponse = new OrderResponse(1L, 1L, 2, now, "Please handle this order with care.");
	}
	
	@Test
	public void testCreateOrder() {
		when(OrderService.createOrder(anyString(), any(OrderRequest.class), any(BindingResult.class)))
		.thenReturn(orderResponse);
		
		ResponseEntity<OrderResponse> response = orderController.createOrdeer("Bearer dummy_token", orderRequest, bindingResult);
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getBody().getId()).isEqualTo(1L);
		assertThat(response.getBody().getOptionId()).isEqualTo(1L);
		assertThat(response.getBody().getQuantity()).isEqualTo(2);
		assertThat(response.getBody().getOrderDateTime()).isEqualTo(now);
		assertThat(response.getBody().getMessage()).isEqualTo("Please handle this order with care.");
	}
}
