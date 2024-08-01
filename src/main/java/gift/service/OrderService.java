package gift.service;

import gift.dto.OrderDto;
import gift.exception.CustomNotFoundException;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.OrderRepository;
import gift.repository.ProductOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductOptionRepository productOptionRepository;

  @Autowired
  public OrderService(OrderRepository orderRepository, ProductOptionRepository productOptionRepository) {
    this.orderRepository = orderRepository;
    this.productOptionRepository = productOptionRepository;
  }

  public OrderDto createOrder(OrderDto orderDto) {
    ProductOption productOption = productOptionRepository.findById(orderDto.getOptionId())
            .orElseThrow(() -> new CustomNotFoundException("Option not found"));
    Order order = new Order(productOption, orderDto.getQuantity(), orderDto.getMessage());
    Order savedOrder = orderRepository.save(order);
    return new OrderDto(savedOrder.getId(), savedOrder.getProductOption().getId(), savedOrder.getQuantity(), savedOrder.getOrderDateTime(), savedOrder.getMessage());
  }

  public Page<OrderDto> getOrders(PageRequest pageRequest) {
    return orderRepository.findAll(pageRequest).map(order ->
            new OrderDto(order.getId(), order.getProductOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage()));
  }
}