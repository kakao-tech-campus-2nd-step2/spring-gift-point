package gift.doamin.order.service;

import gift.doamin.order.dto.OrderRequest;
import gift.doamin.order.dto.OrderResponse;
import gift.doamin.order.entity.Order;
import gift.doamin.order.repository.OrderRepository;
import gift.doamin.product.entity.Option;
import gift.doamin.product.repository.OptionRepository;
import gift.doamin.user.dto.UserDto;
import gift.doamin.user.entity.User;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.UserRepository;
import gift.doamin.wishlist.service.WishListService;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final KakaoMessageService kakaoMessageService;
    private final WishListService wishListService;


    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository,
        UserRepository userRepository, KakaoMessageService kakaoMessageService,
        WishListService wishListService) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.wishListService = wishListService;
    }

    @Transactional
    public OrderResponse makeOrder(UserDto userDto, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(() ->
            new NoSuchElementException("해당 옵션이 존재하지 않습니다."));
        User user = userRepository.findById(userDto.getId())
            .orElseThrow(UserNotFoundException::new);
        Order order = orderRepository.save(
            new Order(user, user, option, orderRequest.getQuantity(), orderRequest.getMessage()));

        option.subtract(orderRequest.getQuantity());

        user.subtractPoint(option.getProduct().getPrice());

        wishListService.subtractWishList(user.getId(), option.getId(), orderRequest.getQuantity());

        try {
            kakaoMessageService.sendMessage(order.getReceiver(), order.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return new OrderResponse(order);
    }

    public Page<OrderResponse> getPage(Long userId, Pageable pageable) {
        return orderRepository.findAllBySenderId(userId, pageable).map(OrderResponse::new);
    }
}
