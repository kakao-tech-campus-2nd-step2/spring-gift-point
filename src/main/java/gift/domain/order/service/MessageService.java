package gift.domain.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Member;
import gift.domain.member.entity.OauthToken;
import gift.domain.member.service.OauthTokenService;
import gift.domain.order.dto.MultipleOrderResponse;
import gift.exception.ExternalApiException;
import gift.external.api.kakao.dto.FeedObjectRequest;
import gift.external.api.kakao.dto.FeedObjectRequest.Button;
import gift.external.api.kakao.dto.FeedObjectRequest.Content;
import gift.external.api.kakao.dto.FeedObjectRequest.Link;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    private final MessageApiProvider<String> messageApiProvider;
    private final OauthTokenService oauthTokenService;
    private final ObjectMapper objectMapper;

    public MessageService(
        MessageApiProvider<String> messageApiProvider,
        OauthTokenService oauthTokenService,
        ObjectMapper objectMapper
    ) {
        this.messageApiProvider = messageApiProvider;
        this.oauthTokenService = oauthTokenService;
        this.objectMapper = objectMapper;
    }

    public String sendMessageToMe(Member member, MultipleOrderResponse orderResponse) {
        String tempLinkUrl = "http://localhost:8080/api/products/" + orderResponse.orderItems().get(0).product().id();
        FeedObjectRequest templateObject = new FeedObjectRequest(
            "feed",
            new Content(
                "주문해 주셔서 감사합니다.",
                orderResponse.orderItems().get(0).product().imageUrl(),
                orderResponse.recipientMessage(),
                new Link(tempLinkUrl)
            ),
            new Button[]{
                new Button(
                    "자세히 보기",
                    new Link(tempLinkUrl)
                )
            }
        );

        OauthToken oauthToken = oauthTokenService.getOauthToken(member, AuthProvider.KAKAO);

        try {
            String serialized = objectMapper.writeValueAsString(templateObject);
            String response = messageApiProvider.sendMessageToMe(oauthToken.getAccessToken(), serialized);

            if (!response.equals("0")) {
                throw new ExternalApiException("error.kakao.talk.message.response");
            };

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
