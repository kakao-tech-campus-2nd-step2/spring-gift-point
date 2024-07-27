package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Order;
import gift.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptionService optionService;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void PlaceOrderSuccess() {
        OrderRequest orderRequest = new OrderRequest(1L, 1L, 1, "Test message", LocalDateTime.now(), "user@example.com");

        Order order = new Order.Builder()
                .id(1L)
                .optionId(1L)
                .quantity(2)
                .orderTime(LocalDateTime.now())
                .message("Test message")
                .email("user@example.com")
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.placeOrder(orderRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getOptionId());
        assertEquals(2, response.getQuantity());
        assertEquals("test message", response.getMessage());

         verify(optionService, times(1)).decreaseOptionQuantity(1L, 2);
         verify(orderRepository, times(1)).save(any(Order.class));
    }
}
