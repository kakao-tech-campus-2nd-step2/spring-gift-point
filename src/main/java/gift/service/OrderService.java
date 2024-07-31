package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.exception.ErrorCode;
import gift.exception.customException.CustomNotFoundException;
import gift.model.dto.OrderDTO;
import gift.model.entity.Option;
import gift.model.entity.Order;
import gift.model.entity.User;
import gift.oauth.KakaoApiService;
import gift.repository.ItemRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final KakaoApiService kakaoApiService;
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final ItemRepository itemRepository;

    public OrderService(UserRepository userRepository,
        KakaoApiService kakaoApiService,
        OrderRepository orderRepository, OptionRepository optionRepository,
        ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.kakaoApiService = kakaoApiService;
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrderDTO executeOrder(Long userId, OrderDTO orderDTO)
        throws JsonProcessingException {
        User user = findUserByUserId(userId);
        Option option = findOptionByOptionId(orderDTO.getOptionId());
        option.decreaseQuantity(orderDTO.getQuantity());
        kakaoApiService.sendMessageToMe(user.getAccessToken(), orderDTO.getMessage());
        Order order = user.addOrder(new Order(user, orderDTO.getOptionId(),
            orderDTO.getQuantity(), orderDTO.getMessage(), orderDTO.getOrderDateTime()));
        return new OrderDTO(orderRepository.save(order));
    }

    public User findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomNotFoundException(
            ErrorCode.USER_NOT_FOUND));
    }

    public Option findOptionByOptionId(Long optionId) {
        return optionRepository.findById(optionId)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.OPTION_NOT_FOUND));
    }
}
