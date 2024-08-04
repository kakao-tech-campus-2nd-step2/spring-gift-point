package gift.order.service;

import gift.order.domain.Order;
import gift.order.domain.OrderListResponse;
import gift.order.domain.OrderResponse;
import gift.order.repository.OrderRepository;
import gift.payment.domain.PaymentRequest;
import gift.payment.domain.PaymentResponse;
import gift.product.application.ProductService;
import gift.product.application.WishListService;
import gift.product.domain.Product;
import gift.product.domain.ProductListResponse;
import gift.product.domain.ProductResponse;
import gift.user.application.KakaoOauthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final KakaoAllimService allimService;

    private final WishListService wishListService;
    private final KakaoOauthService kakaoOauthService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, KakaoAllimService allimService, WishListService wishListService, KakaoOauthService kakaoOauthService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.allimService = allimService;
        this.wishListService = wishListService;
        this.kakaoOauthService = kakaoOauthService;
        this.productService = productService;
    }

    public PaymentResponse createOrder(Long userId, PaymentRequest request) {
        // 위시리스트에 있는 상품을 주문할 경우 위시리스트에서 삭제
        wishListService.deleteProductFromWishList(userId, request.getOptionId());

        // 주문 생성
        Order order = orderRepository.save(new Order(userId, request));
        return order.toOrderCreateResponse();
    }

    public OrderListResponse getOrders(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        List<Long> productIds = orders.getContent().stream()
                .map(Order::getProductId)
                .collect(Collectors.toList());
        Map<Long, Product> products = productService.getProductsByIds(productIds);

        List<OrderResponse> orderResponses = orders.getContent().stream()
                .map(order -> {
                    Product product = products.get(order.getProductId());
                    return new OrderResponse(
                            order.getId(),
                            order.getProductId(),
                            product.getName(),
                            order.getOptionId(),
                            order.getQuantity(),
                            product.getImageUrl(),
                            product.getPrice(),
                            order.getOrderDateTime().toString(),
                            order.getMessage(),
                            true
                    );
                })
                .collect(Collectors.toList());

        return new OrderListResponse(
                orderResponses,
                orders.getNumber(),
                orders.getTotalElements(),
                orders.getNumberOfElements(),
                orders.isLast()
        );
    }
}
