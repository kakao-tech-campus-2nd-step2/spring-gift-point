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
import jakarta.servlet.http.HttpSession;
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
    private final HttpSession session;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OptionRepository optionRepository,
        UserRepository userRepository, WishlistRepository wishlistRepository,
        KakaoLoginService kakaoLoginService, HttpSession session) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.kakaoLoginService = kakaoLoginService;
        this.session = session;
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken) {
        return placeOrder(kakaoUserDTO, wishlistId, accessToken, 0); // 포인트 사용 없이 주문
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken, int pointsToUse) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 위시리스트 ID: " + wishlistId));

        SiteUser user = userRepository.findByUsername(kakaoUserDTO.getProperties().getNickname())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + kakaoUserDTO.getProperties().getNickname()));

        if (pointsToUse > user.getPoints()) {
            throw new IllegalArgumentException("사용할 포인트가 보유한 포인트보다 많습니다.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDateTime(LocalDateTime.now());

        WishlistDTO wishlistDTO = WishlistDTO.convertToDTO(wishlist);
        int totalPrice = wishlistDTO.getTotalPrice() - pointsToUse; // 포인트 차감하여 총 결제 금액 계산

        for (WishlistDTO.OptionDTO optionDTO : wishlistDTO.getOptions()) {
            Option option = optionRepository.findById(optionDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 옵션 ID: " + optionDTO.getId()));
            order.setOption(option);
            order.setQuantity(optionDTO.getQuantity());

            option.setMaxQuantity(option.getMaxQuantity() - optionDTO.getQuantity());
            optionRepository.save(option);
            orderRepository.save(order);
        }

        // 포인트 차감 및 적립
        int pointsBeforeUse = user.getPoints();
        user.setPoints(pointsBeforeUse - pointsToUse); // 포인트 차감
        logger.info("포인트 차감: {} -> {}", pointsBeforeUse, user.getPoints());

        int pointsToEarn = (int) (totalPrice * 0.1); // 결제 금액의 10%를 포인트로 적립
        int pointsBeforeEarn = user.getPoints();
        user.setPoints(pointsBeforeEarn + pointsToEarn);
        userRepository.save(user);
        logger.info("포인트 적립: {} -> {}", pointsBeforeEarn, user.getPoints());

        // 세션에 포인트 업데이트
        session.setAttribute("points", user.getPoints());

        String message = createMessage(kakaoUserDTO, wishlistDTO);
        kakaoLoginService.sendMessage(accessToken, message);

        hideWishlistItem(wishlistId);

        OrderDTO orderDTO = OrderDTO.from(order);
        orderDTO.setNewPoints(user.getPoints());
        return orderDTO;
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

    private void hideWishlistItem(Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 위시리스트 ID: " + wishlistId));
        wishlist.setHidden(true);
        wishlistRepository.save(wishlist);
    }
}
