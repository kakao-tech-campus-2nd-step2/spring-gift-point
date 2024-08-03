package gift.service;

import gift.common.exception.ProductNotFoundException;
import gift.common.exception.UserNotFoundException;
import gift.common.utils.JwtUtil;
import gift.model.option.Option;
import gift.model.order.Order;
import gift.model.order.OrderRequest;
import gift.model.order.OrderResponse;
import gift.model.product.Product;
import gift.model.user.User;
import gift.repository.option.OptionRepository;
import gift.repository.order.OrderRepository;
import gift.repository.product.ProductRepository;
import gift.repository.user.UserRepository;
import gift.repository.wish.WishListRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    private final KakaoMessageService kakaoMessageService;
    private final JwtUtil jwtUtil;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        UserRepository userRepository, ProductRepository productRepository,
        WishListRepository wishListRepository,
        KakaoMessageService kakaoMessageService, JwtUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.wishListRepository = wishListRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.jwtUtil = jwtUtil;
    }

    public OrderResponse createOrder(String token, OrderRequest orderRequest) {
        String email = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(email);


        Option option = optionRepository.findById(orderRequest.optionId()).orElseThrow();
        option.subtract(orderRequest.quantity());
        optionRepository.save(option);

        Order order = new Order(user, option, orderRequest.productId(), orderRequest.quantity(), orderRequest.message());
        orderRepository.save(order);

        // 포인트 사용
        int price = option.getQuantity() * option.getProduct().getPrice();
        if(price > user.getPoint()) {// 가격보다 사용자의 포인트가 적다면
            user.subtractPoints(user.getPoint()); // 사용자의 포인트 전액 사용
            userRepository.save(user);
        }
        if (price <= user.getPoint()) { // 가격보다 사용자의 포인트가 크다면
            user.subtractPoints(price); // 가격만큼의 사용자 포인트 사용
            userRepository.save(user);
        }


        // 위시리스트에 주문 상품이 존재하면 위시리스트에서 삭제
        if(wishListRepository.existsByUserAndProduct(user, option.getProduct())) {
            wishListRepository.deleteByUserIdAndAndProductId(user.getId(),
                orderRequest.productId());
        }

        // 카카오톡 나에게 메세지 보내기 기능으로 주문 내역 메세지 보내기
        kakaoMessageService.sendMessageToMe(token, order);
        return OrderResponse.from(order);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }


}
