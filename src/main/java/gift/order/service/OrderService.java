package gift.order.service;

import gift.order.domain.CreateOrderRequest;
import gift.order.domain.Order;
import gift.order.domain.OrderCreateResponse;
import gift.order.repository.OrderRepository;
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

    public OrderCreateResponse createOrder(CreateOrderRequest request, String accessToken) {
        Long userId = kakaoOauthService.getKakaoUserProfile(accessToken).getId();
        wishListService.deleteProductFromWishList(userId, request.getOptionId());

        Order order = orderRepository.save(new Order(userId, request));
        allimService.sendAllim(accessToken, order.toString());

        return order.toOrderCreateResponse();
    }
}
