package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.KakaoProperties;
import gift.api.OrderRequest;
import gift.api.OrderResponse;
import gift.converter.OrderConverter;
import gift.dto.OrderDTO;
import gift.model.User;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public OrderResponse createOrder(String authorization, OrderDTO orderDTO) {
        // Option 수량 차감
        boolean updated = optionService.decreaseOptionQuantity(orderDTO.getOptionId(), orderDTO.getQuantity());

        if (!updated) {
            throw new IllegalArgumentException("Insufficient product quantity.");
        }

        OrderRequest order = new OrderRequest(
            orderDTO.getOptionId(),
            orderDTO.getQuantity(),
            orderDTO.getMessage()
        );
        orderRepository.save(order);
        orderDTO.setOrderId(order.getOrderId());

        // 카카오톡 메시지 전송
        String token = authorization.replace("Bearer ", "");
        OrderResponse orderResponse = kakaoMessageService.sendKakaoMessage(token, orderDTO);


        // access token에서 이메일 추출
        String email = kakaoService.getUserEmail(token);

        Long productId = optionService.getProductIdByOptionId(orderDTO.getOptionId());

        if (wishListService.isProductInWishList(email, productId)){
            wishListService.removeProductFromWishList(email, productId);
        }

        return orderResponse;
    }
}