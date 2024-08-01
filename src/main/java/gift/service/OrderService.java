package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.model.Order;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final WishlistRepository wishlistRepository;
  private final KakaoService kakaoService;

  @Autowired
  public OrderService(OrderRepository orderRepository, WishlistRepository wishlistRepository, KakaoService kakaoService) {
    this.orderRepository = orderRepository;
    this.wishlistRepository = wishlistRepository;
    this.kakaoService = kakaoService;
  }

  @Transactional
  public OrderResponse placeOrder(OrderRequest orderRequest, String accessToken) {
    Order order = new Order(orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage());
    orderRepository.save(order);

    wishlistRepository.deleteByOptionId(orderRequest.getOptionId());

    try {
      kakaoService.sendMessage(orderRequest.getMessage(), accessToken);
    } catch (ResponseStatusException e) {
      handleException(e);
    }

    return new OrderResponse(order.getId(), order.getOptionId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
  }

  private void handleException(ResponseStatusException e) {
    if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: Invalid or expired access token");
    } else {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send message", e);
    }
  }
}