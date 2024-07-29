package gift.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.properties.KakaoProperties;
import gift.dto.giftorder.GiftOrderResponse;
import gift.dto.kakao.KakaoAuthResponse;
import gift.dto.kakao.KakaoTokenResponse;
import gift.dto.kakao.template.KakaoTemplate;
import gift.dto.kakao.template.KakaoTemplateCommerce;
import gift.dto.kakao.template.KakaoTemplateContent;
import gift.dto.kakao.template.KakaoTemplateLink;
import gift.exception.BadRequestException;
import gift.exception.InvalidKakaoTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoApiClient {

    private final RestClient restClient;
    private final KakaoProperties kakaoProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다. 갱신이 필요합니다.";
    private static final String TOKEN_BASE_URL = "https://kauth.kakao.com/oauth/token";

    public KakaoApiClient(KakaoProperties kakaoProperties, RestClient restClient) {
        this.kakaoProperties = kakaoProperties;
        this.restClient = restClient;
    }

    public KakaoTokenResponse getTokenResponse(String code, String redirectUri) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.restApiKey());
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        var response = restClient.post()
                .uri(URI.create(TOKEN_BASE_URL))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(String.class);

        return convertDtoWithJsonString(response, KakaoTokenResponse.class);
    }

    public KakaoTokenResponse getRefreshedTokenResponse(String refreshToken) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoProperties.restApiKey());
        body.add("refresh_token", refreshToken);

        var response = restClient.post()
                .uri(URI.create(TOKEN_BASE_URL))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(statusCode -> statusCode.equals(HttpStatus.UNAUTHORIZED), (req, res) -> {
                    throw new InvalidKakaoTokenException(INVALID_TOKEN_MESSAGE);
                })
                .onStatus(statusCode -> statusCode.equals(HttpStatus.BAD_REQUEST), (req, res) -> {
                    throw new InvalidKakaoTokenException(INVALID_TOKEN_MESSAGE);
                })
                .body(String.class);

        return convertDtoWithJsonString(response, KakaoTokenResponse.class);
    }

    public KakaoAuthResponse getKakaoAuthResponse(KakaoTokenResponse kakaoTokenResponse) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var header = "Bearer " + kakaoTokenResponse.accessToken();

        var response = restClient.get()
                .uri(URI.create(url))
                .header("Authorization", header)
                .retrieve()
                .body(String.class);

        return convertDtoWithJsonString(response, KakaoAuthResponse.class);
    }

    public void sendSelfMessageOrder(String accessToken, GiftOrderResponse giftOrderResponse) {
        try {
            var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
            var header = "Bearer " + accessToken;

            var template = getCommerceTemplate(giftOrderResponse);
            var body = new LinkedMultiValueMap<String, Object>();
            body.add("template_object", objectMapper.writeValueAsString(template));

            restClient.post()
                    .uri(URI.create(url))
                    .header("Authorization", header)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .onStatus(statusCode -> statusCode.equals(HttpStatus.UNAUTHORIZED), (req, res) -> {
                        throw new InvalidKakaoTokenException(INVALID_TOKEN_MESSAGE);
                    })
                    .body(String.class);
        } catch (JsonProcessingException exception) {
            throw new BadRequestException("잘못된 입력으로 인해 JSON 파싱에 실패했습니다" + exception.getMessage());
        }
    }

    private <T> T convertDtoWithJsonString(String response, Class<T> returnTypeClass) {
        try {
            return objectMapper.readValue(response, returnTypeClass);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(returnTypeClass.getName() + "의 데이터를 DTO 로 변환하는 과정에서 예외가 발생했습니다.", exception);
        }
    }

    private KakaoTemplate getCommerceTemplate(GiftOrderResponse giftOrderResponse) {
        var objectType = "commerce";
        var link = new KakaoTemplateLink("https://gift.kakao.com/product/2370524");
        var content = new KakaoTemplateContent(giftOrderResponse.message(), "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240417111629_616eccb9d4cd464fa06d3430947dce15.jpg", giftOrderResponse.message(), link);
        var commerce = new KakaoTemplateCommerce(giftOrderResponse.optionInformation().productName() + "[" + giftOrderResponse.optionInformation().name() + "]", giftOrderResponse.optionInformation().price() * giftOrderResponse.quantity());
        return new KakaoTemplate(objectType, content, commerce);
    }
}
