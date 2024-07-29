package gift.order;

import gift.oauth.KakaoOauthService;
import gift.order.dto.CreateOrderRequestDTO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderInterceptor {

    private final KakaoOauthService kakaoOauthService;

    public OrderInterceptor(
        KakaoOauthService kakaoOauthService
    ) {
        this.kakaoOauthService = kakaoOauthService;
    }

    @Async
    @AfterReturning(
        pointcut = "execution(public * gift.order.OrderService.createOrder(..)) && args(createOrderRequestDTO, accessToken)",
        argNames = "createOrderRequestDTO, accessToken"
    )
    public void sendMessageToKakao(
        CreateOrderRequestDTO createOrderRequestDTO,
        String accessToken
    ) {
        kakaoOauthService.sendMessage(createOrderRequestDTO.getMessage(), accessToken);
    }
}
