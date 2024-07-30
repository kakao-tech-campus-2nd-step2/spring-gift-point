package gift.service;

import gift.AuthorizationToken;
import gift.api.OrderRequest;
import gift.converter.OrderConverter;
import gift.dto.OrderDTO;
import gift.model.User;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OptionService optionService;
    private final UserService userService;
    private final WishListService wishListService;
    private final KakaoService kakaoService;
    private final KakaoMessageService kakaoMessageService;
    private final OrderRepository orderRepository;

    public OrderService(OptionService optionService, UserService userService,
        WishListService wishListService, KakaoService kakaoService, KakaoMessageService kakaoMessageService, OrderRepository orderRepository) {
        this.optionService = optionService;
        this.userService = userService;
        this.wishListService = wishListService;
        this.kakaoService = kakaoService;
        this.kakaoMessageService = kakaoMessageService;
        this.orderRepository = orderRepository;
    }

    public String createOrder(String authorization, OrderDTO orderDTO) {
        AuthorizationToken token = new AuthorizationToken(authorization);

        // Option 수량 차감
        boolean updated = optionService.decreaseOptionQuantity(orderDTO.getOptionId(), orderDTO.getQuantity());

        if (!updated) {
            throw new IllegalArgumentException("Insufficient product quantity.");
        }

        // 카카오톡 메시지 전송
        boolean messageSent = kakaoMessageService.sendKakaoMessage(token.getToken(), orderDTO);

        if (!messageSent) {
            throw new RuntimeException("Order created but failed to send message.");
        }

        // access token에서 이메일 추출
        String email = kakaoService.getUserEmail(token.getToken());

        // 이메일로 사용자 조회
        User user = userService.findByEmail(email);

        // DTO를 엔티티로 변환
        OrderRequest orderRequest = OrderConverter.convertToEntity(orderDTO);

        // 저장
        orderRepository.save(orderRequest);

        if (wishListService.isProductInWishList(email, orderDTO.getOrderId())) {
            // 위시리스트에서 주문한 제품 ID 삭제
            wishListService.removeProductFromWishList(email, orderDTO.getOrderId());
        }

        return "Order created and message sent.";
    }
}