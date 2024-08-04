package gift.service;

import gift.dto.PointHistory.PointHistoryDto;
import gift.dto.kakao.KakaoErrorCode;
import gift.dto.order.OrderDTO;
import gift.entity.*;
import gift.exception.KakaoException;
import gift.exception.ResourceNotFoundException;
import gift.repository.*;
import jakarta.persistence.LockModeType;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final RestClient client = RestClient.builder().build();

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OptionService optionService;
    private final ProductWishlistRepository productWishlistRepository;
    private final ProductService productService;
    private final UserService userService;
    private final PointHistoryRepository pointHistoryRepository;

    @Autowired
    public OrderService(UserRepository userRepository, WishlistRepository wishlistRepository, OrderRepository orderRepository, ProductOptionRepository productOptionRepository, OptionService optionService, ProductWishlistRepository productWishlistRepository, ProductService productService, UserService userService, PointHistoryRepository pointHistoryRepository) {
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.optionService = optionService;
        this.productWishlistRepository = productWishlistRepository;
        this.productService = productService;
        this.userService = userService;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Lock(LockModeType.OPTIMISTIC)
    public synchronized Order save(HttpSession session, OrderDTO orderDTO) {
        String email = (String) session.getAttribute("email");

        ProductOption productOption = productOptionRepository
                .findByProductIdAndOptionId(orderDTO.getProduct_id(), orderDTO.getOption_id())
                .orElseThrow(() -> new ResourceNotFoundException("Illegal order"));

        User user = userService.findOne(email);

        Product product = productOption.getProduct();
        Option option = productOption.getOption();

        PointHistoryDto pointHistoryDto = new PointHistoryDto();
        pointHistoryDto.setUserId(user.getId());
        pointHistoryDto.setPreviousPoints(user.getPoint());

        // 포인트 사용 가능 조건
        int totalPrice = option.getQuantity() * product.getPrice();
        if (orderDTO.getPoint() > user.getPoint() || orderDTO.getPoint() > totalPrice) {
            throw new IllegalArgumentException("Illegal order");
        }
        pointHistoryDto.setChangePoints(orderDTO.getPoint());
        pointHistoryDto.setCurrentPoints(user.getPoint() - orderDTO.getPoint());

        // 포인트 차감
        user.subtractPoint(orderDTO.getPoint());
        userRepository.save(user);

        // 해당 옵션 수량 차감
        optionService.subtract(option.getId(), orderDTO.getQuantity());

        // 위시리스트에 있으면 삭제
        Optional<Wishlist> wishlist = wishlistRepository.findByEmail(email);
        if (wishlist.isPresent()) {
            productWishlistRepository.deleteByProductIdAndWishlistId(orderDTO.getProduct_id(), wishlist.get().getId());
        }

        Order order = new Order(orderDTO);
        order.setUser(user);

        Order result = orderRepository.save(order);
        pointHistoryRepository.save(new PointHistory(pointHistoryDto));

        // 카톡 나에게 전송
        String kakaoAccessToken = (String) session.getAttribute("kakaoAccessToken");
        if (kakaoAccessToken != null) {
            sendToMe(kakaoAccessToken, orderDTO);
        }

        return result;
    }

    public List<Order> findAll(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByUserId(user.getId());
    }

    @Transactional
    public void delete(Long orderId, String email) {
        User user = userService.findOne(email);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (user.getId() != order.getUser().getId()) {
            throw new KakaoException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        orderRepository.delete(order);
    }

    public void sendToMe(String kakaoAccessToken, OrderDTO order) {
        LinkedMultiValueMap<String, String> body = makeRequestBody(order);

        String response;

        try {
            response = client.post()
                    .uri(URI.create("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
                    .header("Authorization", "Bearer " + kakaoAccessToken)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            String statusCode = e.getMessage().split(" ")[0];
            KakaoErrorCode errorCode = KakaoErrorCode.determineKakaoErrorCode(statusCode);
            throw new KakaoException(errorCode.getStatus(), errorCode.getMessage());
        }
    }

    private LinkedMultiValueMap<String, String> makeRequestBody(OrderDTO order) {
        String redirect_url = "/me";
        Product product = productService.findById(order.getProduct_id());
        String msg = product.getName() + " : " + Integer.toString(order.getQuantity()) + "\n" + order.getMessage();

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        JSONObject linkObj = new JSONObject();
        linkObj.put("web_url", redirect_url);
        linkObj.put("mobile_web_url", redirect_url);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("object_type", "text");
        jsonObj.put("text", msg);
        jsonObj.put("link", linkObj);

        body.add("template_object", jsonObj.toString());
        return body;
    }
}
