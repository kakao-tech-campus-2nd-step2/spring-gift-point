package gift.service;

import gift.domain.OptionDTO;
import gift.domain.OrderDTO;
import gift.entity.Order;
import gift.entity.Option;
import gift.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private KakaoLoginService kakaoLoginService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrder() {
        // Given
        String token = "testToken";
        OptionDTO optionDTO = new OptionDTO("test", 1);
        Option option = new Option(optionDTO);

        OrderDTO orderDTO = new OrderDTO(1, 2, "Test message");
        Order order = new Order(option, 2, "timestamp", "Test message");

        option.setId(1);


        when(optionRepository.findById(1)).thenReturn(Optional.of(option));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        Order result = orderService.addOrder(token, orderDTO);

        // Then
        assertNotNull(result);
        assertEquals(option, result.getOption());
        assertEquals(2, result.getOption().getQuantity());
        assertEquals("Test message", result.getMessage());

        verify(orderRepository).save(any(Order.class));
        verify(kakaoLoginService).sendMessage(token, "Test message");
        verify(optionRepository).findById(1);
    }

    @Test
    void testAddOrderOptionNotFound() {
        // Given
        String token = "testToken";
        OrderDTO orderDTO = new OrderDTO(1, 2, "Test message");

        when(optionRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> orderService.addOrder(token, orderDTO));
        verify(optionRepository).findById(1);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testFindOrderById() {
        // Given
        int orderId = 1;
        OptionDTO optionDTO = new OptionDTO("test", 1);
        Option option = new Option(optionDTO);

        Order order = new Order(option, 2, "timestamp", "Test message");

        option.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        Order result = orderService.findOrderById(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void testFindOrderByIdNotFound() {
        // Given
        int orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> orderService.findOrderById(orderId));
        verify(orderRepository).findById(orderId);
    }
}