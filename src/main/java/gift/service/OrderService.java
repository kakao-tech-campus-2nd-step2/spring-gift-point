package gift.service;

import gift.dto.OrderDto;
import gift.exception.CustomNotFoundException;
import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductOptionRepository productOptionRepository;
  private final MemberRepository memberRepository;

  @Autowired
  public OrderService(OrderRepository orderRepository, ProductOptionRepository productOptionRepository, MemberRepository memberRepository) {
    this.orderRepository = orderRepository;
    this.productOptionRepository = productOptionRepository;
    this.memberRepository = memberRepository;
  }

  @Transactional
  public OrderDto createOrder(OrderDto orderDto) {
    ProductOption productOption = productOptionRepository.findById(orderDto.getOptionId())
            .orElseThrow(() -> new CustomNotFoundException("Option not found"));
    Member member = memberRepository.findById(orderDto.getMemberId())
            .orElseThrow(() -> new CustomNotFoundException("Member not found"));

    int totalPrice = productOption.getProduct().getPrice() * orderDto.getQuantity();
    int pointsUsed = 0;
    int remainPoints = member.getPoints();

    if (orderDto.isUsePoint() && member.getPoints() > 0) {
      pointsUsed = Math.min(totalPrice, member.getPoints());
      totalPrice -= pointsUsed;
      member.deductPoints(pointsUsed);
      remainPoints = member.getPoints();
    }

    int pointsEarned = (int) (totalPrice * 0.1);
    member.addPoints(pointsEarned);

    Order order = new Order(productOption, member, orderDto.getQuantity(), orderDto.getMessage(), LocalDateTime.now(), pointsUsed, pointsEarned);
    Order savedOrder = orderRepository.save(order);
    memberRepository.save(member);

    return new OrderDto(savedOrder.getId(), savedOrder.getProductOption().getId(), savedOrder.getQuantity(), savedOrder.getOrderDateTime(), savedOrder.getMessage(), pointsUsed, pointsEarned, member.getPoints(), member.getId(), orderDto.isUsePoint(), totalPrice, pointsUsed, remainPoints);
  }

  @Transactional(readOnly = true)
  public Page<OrderDto> getOrders(Pageable pageable) {
    Page<Order> orders = orderRepository.findAll(pageable);
    List<OrderDto> orderDtos = orders.stream()
            .map(order -> new OrderDto(
                    order.getId(),
                    order.getProductOption().getId(),
                    order.getQuantity(),
                    order.getOrderDateTime(),
                    order.getMessage(),
                    order.getPointsUsed(),
                    order.getPointsEarned(),
                    order.getMember().getPoints(),
                    order.getMember().getId(),
                    order.getPointsUsed() > 0,
                    order.getProductOption().getProduct().getPrice() * order.getQuantity(),
                    order.getPointsUsed(),
                    order.getMember().getPoints()
            ))
            .collect(Collectors.toList());
    return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
  }
}