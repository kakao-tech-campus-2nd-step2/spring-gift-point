package gift.service;

import gift.exception.ResourceNotFoundException;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import gift.order.service.OrderService;
import gift.product.entity.Option;
import gift.product.repository.OptionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OptionRepository optionRepository;

  @InjectMocks
  private OrderService orderService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetOrders() {
    Pageable pageable = PageRequest.of(0, 10);
    Order order = Order.builder()
        .option(new Option())
        .quantity(1)
        .message("Test Order")
        .build();
    Page<Order> orders = new PageImpl<>(List.of(order), pageable, 1);
    when(orderRepository.findAll(pageable)).thenReturn(orders);

    Page<OrderResponseDto> result = orderService.getOrders(pageable);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getContent().size());
    assertEquals("Test Order", result.getContent().get(0).message());
  }

  @Test
  public void testCreateOrderWhenOptionExists() {
    Long optionId = 1L;
    OrderRequestDto orderRequestDto = new OrderRequestDto(optionId, 5, "Special message");
    Option option = new Option();
    option.setId(optionId);
    Order order = Order.builder()
        .option(option)
        .quantity(5)
        .message("Special message")
        .build();
    when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
    when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

    OrderResponseDto result = orderService.createOrder(orderRequestDto);

    assertNotNull(result);
    assertEquals(5, result.quantity());
    assertEquals("Special message", result.message());
    verify(optionRepository, times(1)).findById(optionId);
    verify(orderRepository, times(1)).save(any(Order.class));
  }

  @Test
  public void testCreateOrderWhenOptionDoesNotExist() {
    Long optionId = 1L;
    OrderRequestDto orderRequestDto = new OrderRequestDto(optionId, 5, "Special message");
    when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

    ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
      orderService.createOrder(orderRequestDto);
    });

    assertEquals("옵션을 찾을 수 없습니다.", thrown.getMessage());
  }
}
