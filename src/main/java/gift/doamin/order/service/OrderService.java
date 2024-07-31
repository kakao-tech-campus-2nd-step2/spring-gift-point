package gift.doamin.order.service;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.doamin.order.dto.Link;
import gift.doamin.order.dto.MessageTemplate;
import gift.doamin.order.dto.OrderRequest;
import gift.doamin.order.dto.OrderResponse;
import gift.doamin.order.entity.Order;
import gift.doamin.order.repository.OrderRepository;
import gift.doamin.product.entity.Option;
import gift.doamin.product.repository.OptionRepository;
import gift.doamin.user.entity.KakaoOAuthToken;
import gift.doamin.user.entity.User;
import gift.doamin.user.repository.KakaoOAuthTokenRepository;
import gift.doamin.user.service.OAuthService;
import gift.doamin.wishlist.repository.JpaWishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final JpaWishListRepository jpaWishListRepository;
    private final KakaoOAuthTokenRepository oAuthTokenRepository;
    private final OAuthService oAuthService;
    private final RestClient restClient = RestClient.builder().build();

    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository,
        JpaWishListRepository jpaWishListRepository, KakaoOAuthTokenRepository oAuthTokenRepository,
        OAuthService oAuthService) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.jpaWishListRepository = jpaWishListRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oAuthService = oAuthService;
    }

    @Transactional
    public OrderResponse makeOrder(User user, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(() ->
            new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));
        Order order = new Order(user, user, option, orderRequest.getQuantity(),
            orderRequest.getMessage());
        order = orderRepository.save(order);

        option.subtract(orderRequest.getQuantity());

        subtractWishList(user, option, orderRequest.getQuantity());

        sendKakaoTalkMessage(order);

        return new OrderResponse(order);
    }

    @Transactional
    public void subtractWishList(User user, Option option, Integer quantity) {
        jpaWishListRepository.findByUserIdAndProductId(user.getId(), option.getProduct().getId())
            .ifPresent(wish -> {
                try {
                    wish.subtract(quantity);
                } catch (IllegalArgumentException e) {
                    jpaWishListRepository.delete(wish);
                }
            });
    }

    public void sendKakaoTalkMessage(Order order) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        KakaoOAuthToken kakaoOAuthToken = oAuthTokenRepository.findByUser(order.getReceiver())
            .orElseThrow(() ->
                new IllegalArgumentException("카카오톡 사용자에게만 선물할 수 있습니다."));

        oAuthService.renewOAuthTokens(kakaoOAuthToken);

        MultiValueMap<String, Object> body = getMessageRequestBody(order);

        restClient.post()
            .uri(url)
            .contentType(APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + kakaoOAuthToken.getAccessToken())
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }

    private static MultiValueMap<String, Object> getMessageRequestBody(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageTemplate messageTemplate = new MessageTemplate("text", order.getMessage(),
            new Link(""));
        String messageJson;
        try {
            messageJson = objectMapper.writeValueAsString(messageTemplate);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("template_object", messageJson);

        return body;
    }

}
