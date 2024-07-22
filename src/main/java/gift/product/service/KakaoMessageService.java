package gift.product.service;

import gift.config.KakaoMessageConfig;
import gift.product.exception.kakao.KakaoMessageException;
import gift.product.service.command.BuyProductMessageCommand;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class KakaoMessageService {
    private final KakaoMessageConfig kakaoMessageConfig;

    public KakaoMessageService(KakaoMessageConfig kakaoMessageConfig) {
        this.kakaoMessageConfig = kakaoMessageConfig;
    }

    public void sendBuyProductMessage(BuyProductMessageCommand buyProductMessageCommand) {
        var client = kakaoMessageConfig.createMessageSendClient(buyProductMessageCommand.accessToken());
        var body = buyProductMessageCommand.toKakaoMessageTemplate();

        var result = client
                .post()
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new KakaoMessageException();
                }))
                .toEntity(String.class);
    }
}
