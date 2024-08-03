package gift.service;

import gift.dto.OrderResponse;
import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class OrderService {

    private static final String ACCESS_TOKEN_KEY = "accessToken";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private WishService wishService;

    @Autowired
    private KakaoMessageService kakaoMessageService;

    @Autowired
    private PointService pointService;

    @Autowired
    private HttpSession session;

    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(OrderResponse::new);
    }

    @Transactional
    public Order createOrder(Long optionId, int quantity, String message, Member member, int pointsToUse) {
        ProductOption productOption = productOptionService.findProductOptionById(optionId);
        if (productOption == null) {
            throw new IllegalArgumentException("Invalid product option");
        }

        productOptionService.subtractProductOptionQuantity(optionId, quantity);

        // Use points
        if (pointsToUse > 0) {
            pointService.usePoints(member.getId(), pointsToUse);
        }

        Order order = new Order(productOption, member, quantity, message, LocalDateTime.now());

        System.out.println("Original message: " + message);
        System.out.println("Encoded message: " + order.getMessage());

        wishService.deleteWishByProductOptionIdAndMemberId(optionId, member.getId());

        Order savedOrder = orderRepository.save(order);

        String accessToken = (String) session.getAttribute(ACCESS_TOKEN_KEY);
        if (accessToken != null) {
            kakaoMessageService.sendMessage(accessToken, createKakaoMessage(order));
        } else {
            System.out.println("No access token available for Kakao message");
        }

        return savedOrder;
    }

    private String createKakaoMessage(Order order) {
        String productName = order.getProductOption().getName();
        String encodedProductName = java.net.URLEncoder.encode(productName, StandardCharsets.UTF_8);

        return String.format("{\"object_type\":\"text\",\"text\":\"order: %d of %s\",\"link\":{\"web_url\":\"http://localhost:8080/user-products\"}}",
                order.getQuantity(), encodedProductName);
    }
}