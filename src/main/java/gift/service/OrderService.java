package gift.service;

import gift.common.exception.ProductNotFoundException;
import gift.common.exception.UserNotFoundException;
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

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        UserRepository userRepository, ProductRepository productRepository,
        WishListRepository wishListRepository,
        KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.wishListRepository = wishListRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    public OrderResponse createOrder(String token, OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.userId()).orElseThrow(
            () -> new UserNotFoundException("해당 Id를 가진 사용자는 존재하지 않습니다.")
        );

//        Product product = productRepository.findById(orderRequest.productId()).orElseThrow(
//            () -> new ProductNotFoundException("해당 Id를 가진 상품은 존재하지 않습니다.")
//        );
        Option option = optionRepository.findById(orderRequest.optionId()).orElseThrow();
        option.subtract(orderRequest.quantity());
        optionRepository.save(option);

        Order order = new Order(user, option, orderRequest.productId(), orderRequest.quantity(), orderRequest.message());
        orderRepository.save(order);

        // 위시리스트에 주문 상품이 존재하면 위시리스트에서 삭제
        if(wishListRepository.existsByUserAndProduct(user, option.getProduct())) {
            wishListRepository.deleteByUserIdAndAndProductId(orderRequest.userId(),
                orderRequest.productId());
        }

        // 카카오톡 나에게 메세지 보내기 기능으로 주문 내역 메세지 보내기
        kakaoMessageService.sendMessageToMe(token, order);
        return new OrderResponse(order.getId(), option.getId(), order.getQuantity(),
            order.getMessage(), order.getOrderDateTime());
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }


}
