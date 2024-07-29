package gift.service;

import gift.dto.KakaoUserDTO;
import gift.dto.OrderDTO;
import gift.dto.WishlistDTO;
import gift.model.Option;
import gift.model.Order;
import gift.model.SiteUser;
import gift.model.Wishlist;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.UserRepository;
import gift.repository.WishlistRepository;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
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
    public OrderDTO placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken) {
        WishlistDTO wishlistDTO = wishlistRepository.findById(wishlistId)
            .map(WishlistDTO::convertToDTO)
            .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID: " + wishlistId));

        SiteUser user = userRepository.findByUsername(kakaoUserDTO.getProperties().getNickname())
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + kakaoUserDTO.getProperties().getNickname()));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDateTime(LocalDateTime.now());

        for (WishlistDTO.OptionDTO optionDTO : wishlistDTO.getOptions()) {
            Option option = optionRepository.findById(optionDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID: " + optionDTO.getId()));
            order.setOption(option);
            order.setQuantity(optionDTO.getQuantity());

            option.setMaxQuantity(option.getMaxQuantity() - optionDTO.getQuantity());
            optionRepository.save(option);
            orderRepository.save(order);
        }

        String message = createMessage(kakaoUserDTO, wishlistDTO);
        kakaoLoginService.sendMessage(accessToken, message);

        hideWishlistItem(wishlistId);

        return OrderDTO.from(order);
    }

    private String createMessage(KakaoUserDTO kakaoUserDTO, WishlistDTO wishlistDTO) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("<web 발신> [%s]\n%s 님이 상품을 주문하셨습니다.\n", LocalDateTime.now(), kakaoUserDTO.getProperties().getNickname()));

        for (WishlistDTO.OptionDTO option : wishlistDTO.getOptions()) {
            messageBuilder.append(String.format("%s x %d\n", option.getName(), option.getQuantity()));
        }

        messageBuilder.append(String.format("따라서 총 금액은 %d원 입니다.", wishlistDTO.getTotalPrice()));

        return messageBuilder.toString();
    }

    // wishlist 항목 숨김 처리 메서드
    private void hideWishlistItem(Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID: " + wishlistId));
        wishlist.setHidden(true);
        wishlistRepository.save(wishlist);
    }
}
