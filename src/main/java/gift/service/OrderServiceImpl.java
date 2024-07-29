package gift.service;

import gift.dto.KakaoUserDTO;
import gift.dto.Response.OrderResponseDto;
import gift.model.Option;
import gift.model.Order;
import gift.model.SiteUser;
import gift.model.Wishlist;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.UserRepository;
import gift.repository.WishlistRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final KakaoLoginService kakaoLoginService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OptionRepository optionRepository,
        UserRepository userRepository, WishlistRepository wishlistRepository,
        KakaoLoginService kakaoLoginService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.kakaoLoginService = kakaoLoginService;
    }

    @Override
    @Transactional
    public OrderResponseDto placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID: " + wishlistId));

        SiteUser user = userRepository.findByUsername(kakaoUserDTO.getProperties().getNickname())
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + kakaoUserDTO.getProperties().getNickname()));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDateTime(LocalDateTime.now());

        for (Option option : wishlist.getOptions()) {
            order.setOption(option);
            order.setQuantity(option.getQuantity());
            order.setMessage("Some message if needed"); // You can customize this part

            orderRepository.save(order);
        }

        String message = createMessage(kakaoUserDTO, wishlist);
        kakaoLoginService.sendMessage(accessToken, message);

        hideWishlistItem(wishlistId);

        return OrderResponseDto.from(order);
    }

    private String createMessage(KakaoUserDTO kakaoUserDTO, Wishlist wishlist) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("<web 발신> [%s]\n%s 님이 상품을 주문하셨습니다.\n", LocalDateTime.now(), kakaoUserDTO.getProperties().getNickname()));

        for (Option option : wishlist.getOptions()) {
            messageBuilder.append(String.format("%s x %d\n", option.getName(), option.getQuantity()));
        }

        //messageBuilder.append(String.format("따라서 총 금액은 %d원 입니다.", wishlist.getTotalPrice()));

        return messageBuilder.toString();
    }

    private void hideWishlistItem(Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID: " + wishlistId));
        //wishlist.setHidden(true);
        wishlistRepository.save(wishlist);
    }
}
