package gift.order.service;

import gift.exception.InvalidUserInputException;
import gift.order.dto.OrderDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import gift.product.entity.Option;
import gift.product.repository.OptionRepository;
import gift.user.service.KakaoApiService;
import gift.wish.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OptionRepository optionRepository;

  @Autowired
  private WishRepository wishRepository;

  @Autowired
  private KakaoApiService kakaoApiService;

  @Transactional
  public Order createOrder(OrderDto orderDto) {
    Option option = optionRepository.findById(orderDto.getOptionId())
        .orElseThrow(() -> new InvalidUserInputException("유효하지 않은 옵션 ID입니다."));

    option.subtractQuantity(orderDto.getQuantity());
    optionRepository.save(option);

    wishRepository.deleteByProductId(option.getProduct().getId());

    Order order = new Order(orderDto.getOptionId(), orderDto.getQuantity(), orderDto.getMessage());
    order = orderRepository.save(order);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = authentication.getName();

    kakaoApiService.sendOrderMessage(order, userEmail).subscribe();

    return order;
  }
}
