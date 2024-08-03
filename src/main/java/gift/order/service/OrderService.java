package gift.order.service;

import gift.exception.ResourceNotFoundException;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import gift.product.entity.Option;
import gift.product.repository.OptionRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OptionRepository optionRepository;

  public OrderService(OrderRepository orderRepository, OptionRepository optionRepository) {
    this.orderRepository = orderRepository;
    this.optionRepository = optionRepository;
  }

  @Transactional(readOnly = true)
  public Page<OrderResponseDto> getOrders(Pageable pageable) {
    Page<Order> orders = orderRepository.findAll(pageable);
    return orders.map(OrderResponseDto::toDto);
  }

  @Transactional
  public OrderResponseDto createOrder(@Valid OrderRequestDto orderRequestDto) {
    Option option = optionRepository.findById(orderRequestDto.optionId())
        .orElseThrow(() -> new ResourceNotFoundException("옵션을 찾을 수 없습니다."));

    Order order = Order.builder()
        .option(option)
        .quantity(orderRequestDto.quantity())
        .message(orderRequestDto.message())
        .build();

    Order savedOrder = orderRepository.save(order);
    return OrderResponseDto.toDto(savedOrder);
  }

}
