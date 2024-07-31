package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.exception.ErrorCode;
import gift.exception.customException.CustomNotFoundException;
import gift.model.entity.Item;
import gift.model.entity.Order;
import gift.model.dto.OrderDTO;
import gift.model.entity.User;
import gift.oauth.KakaoApiService;
import gift.repository.ItemRepository;
import gift.repository.OrderRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final WishListRepository wishListRepository;
    private final KakaoApiService kakaoApiService;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, ItemRepository itemRepository,
        WishListRepository wishListRepository, KakaoApiService kakaoApiService,
        OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.wishListRepository = wishListRepository;
        this.kakaoApiService = kakaoApiService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderDTO executeOrder(Long userId, OrderDTO orderDTO)
        throws JsonProcessingException {
        User user = findUserByUserId(userId);
        Long targetId = findItemByItemId(orderDTO.getTargetId()).getId();
        Item item = findItemByItemId(orderDTO.getItemId());

        item.getOptionByOptionId(orderDTO.getOptionId())
            .decreaseQuantity(orderDTO.getQuantity());
        if (wishListRepository.existsByUserIdAndItemId(userId, item.getId())) {
            wishListRepository.deleteByUserIdAndItemId(userId, item.getId());
        }
        kakaoApiService.sendMessageToMe(user.getAccessToken(), orderDTO.getMessage());
        Order order = user.addOrder(new Order(user, targetId, item.getId(), orderDTO.getOptionId(),
            orderDTO.getQuantity(), orderDTO.getMessage(), orderDTO.getOrderDateTime()));
        return new OrderDTO(orderRepository.save(order));
    }


    public User findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomNotFoundException(
            ErrorCode.USER_NOT_FOUND));
    }

    public Item findItemByItemId(Long itemId) {
        return itemRepository.findItemByIdWithPLock(itemId)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.ITEM_NOT_FOUND));
    }

}
