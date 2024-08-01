package gift.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import gift.controller.OrderController;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;

public class OrderTest {

	@Mock
	private OrderService orderService;
	
	@InjectMocks
	private OrderController orderController;
	
	@Mock
	private BindingResult bindingResult;
	
	private OrderRequest orderRequest;
	private OrderResponse orderResponse;
	private LocalDateTime now = LocalDateTime.now();
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		
		orderRequest = new OrderRequest(1L, 2, "Please handle this order with care.");
		orderResponse = new OrderResponse(1L, 1L, 2, now, "Please handle this order with care.");
	}
	
	@Test
    public void testGetOrders() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<OrderResponse> orderPage = new PageImpl<>(Collections.singletonList(orderResponse), pageable, 1);
        
		when(orderService.getOrders(any(String.class), any(Pageable.class))).thenReturn(orderPage);
		
		ResponseEntity<Page<OrderResponse>> response = orderController.getOrders("Bearer token", pageable);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(orderResponse.getId());
        assertThat(response.getBody().getContent().get(0).getOptionId()).isEqualTo(orderResponse.getOptionId());
        assertThat(response.getBody().getContent().get(0).getQuantity()).isEqualTo(orderResponse.getQuantity());
        assertThat(response.getBody().getContent().get(0).getOrderDateTime()).isEqualTo(now);
        assertThat(response.getBody().getContent().get(0).getMessage()).isEqualTo(orderResponse.getMessage());
    }
	
	@Test
	public void testCreateOrder() {
		when(orderService.createOrder(anyString(), any(OrderRequest.class), any(BindingResult.class)))
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
