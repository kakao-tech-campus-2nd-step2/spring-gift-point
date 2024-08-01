package gift.order.service;

import gift.order.domain.CreateOrderRequest;
import gift.order.domain.Order;
import gift.order.domain.OrderCreateResponse;
import gift.order.repository.OrderRepository;
import gift.payment.domain.PaymentRequest;
import gift.payment.domain.PaymentResponse;
import gift.product.application.WishListService;
import gift.user.application.KakaoOauthService;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final KakaoAllimService allimService;

    private final WishListService wishListService;
    private final KakaoOauthService kakaoOauthService;

    public OrderService(OrderRepository orderRepository, KakaoAllimService allimService, WishListService wishListService, KakaoOauthService kakaoOauthService) {
        this.orderRepository = orderRepository;
        this.allimService = allimService;
        this.wishListService = wishListService;
        this.kakaoOauthService = kakaoOauthService;
    }

    public PaymentResponse createOrder(Long userId, PaymentRequest request) {
        // 위시리스트에 있는 상품을 주문할 경우 위시리스트에서 삭제
        wishListService.deleteProductFromWishList(userId, request.getOptionId());

        // 주문 생성
        Order order = orderRepository.save(new Order(userId, request));
        return order.toOrderCreateResponse();
    }
}
