package gift.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.domain.dto.response.KakaoUserInfoResponse;
import gift.domain.dto.response.OauthTokenResponse;
import gift.domain.entity.Order;
import gift.domain.entity.Product;
import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OauthService {

    @Value("${oauth.kakao.api_key}")
    private String KAKAO_API_KEY;

    @Value("${oauth.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;

    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    public OauthService(ObjectMapper objectMapper) {
        this.restClient = RestClient.builder().build();
        this.objectMapper = objectMapper;
    }

    public OauthTokenResponse getOauthToken(Type userType, String authorizationCode) {
        if (Objects.requireNonNull(userType) == Type.KAKAO) {
            return getKakaoOauthToken(authorizationCode);
        }
        throw new ServerException(ErrorCode.OAUTH_VENDOR_ILLEGAL);
    }

    public KakaoUserInfoResponse getKakaoUserInfo(String kakaoUserAccessToken) {
        try {
            return restClient
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + kakaoUserAccessToken)
                .retrieve()
                .toEntity(KakaoUserInfoResponse.class)
                .getBody();
        } catch (Exception e) {
            //TODO: 200 응답이 아닐 때를 조금 더 세분화시키기 (https://github.com/kakao-tech-campus-2nd-step2/spring-gift-order/pull/267#discussion_r1692966327)
            throw new ServerException(ErrorCode.TOKEN_UNEXPECTED_ERROR);
        }
    }

    public Integer sendOrderInfoKakaoTalkSelfMessage(String kakaoUserAccessToken, Order order) {

        //카카오톡 셀프 메시지 보내기 위한 클래스 정의
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        class KakaoTalkOrderSelfMessage {

            public final String objectType; // 템플릿 종류 (commerce 고정값)
            public final Content content; //메시지 콘텐츠 정보
            public final Commerce commerce; //상품 이름 및 가격 정보

            public KakaoTalkOrderSelfMessage(Order order) {
                this.objectType = "commerce";
                this.content = new Content(order);
                this.commerce = new Commerce(order);
            }

            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            static class Content {

                public final String title; // 콘텐츠 타이틀
                public final String imageUrl; //콘텐츠 이미지 URL
                public final Integer imageWidth; //콘텐츠 이미지 너비
                public final Integer imageHeight; //콘텐츠 이미지 높이
                //public final String description; //콘텐츠 상세 설명, title과 합쳐서 최대 4줄 표시
                public final Link link; //콘텐츠 클릭시 이동할 링크정보

                public Content(Order order) {
                    Product product = order.getOption().getProduct();
                    this.title = String.format("옵션 \"%s\" %d개를 성공적으로 주문하였습니다. (요청사항: %s)",
                        order.getOption().getName(),
                        order.getQuantity(),
                        order.getMessage());
                    this.imageUrl = product.getImageUrl();
                    this.imageWidth = 800;
                    this.imageHeight = 800;
                    this.link = new Link("http://localhost:8080", "http://localhost:8080");
                }

                @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                static class Link {

                    public final String webUrl;
                    public final String mobileWebUrl;

                    public Link(String webUrl, String mobileWebUrl) {
                        this.webUrl = webUrl;
                        this.mobileWebUrl = mobileWebUrl;
                    }
                }
            }

            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            static class Commerce {

                public final String productName; //상품 이름 및 제목, 최대 2줄 출력, 상품 가격보다 위에 검은글씨로 출력
                public final Integer regularPrice; //정상 가격

                public Commerce(Order order) {
                    Product product = order.getOption().getProduct();
                    this.productName = product.getName();
                    this.regularPrice = product.getPrice();
                }
            }
        }

        try {
            var body = new LinkedMultiValueMap<String, String>();
            body.add("template_object", objectMapper.writeValueAsString(new KakaoTalkOrderSelfMessage(order)));

            String response = restClient
                .post()
                .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + kakaoUserAccessToken)
                .body(body)
                .retrieve()
                .toEntity(String.class)
                .getBody();

            return objectMapper.readTree(response).get("result_code").asInt();

        } catch (Exception e) {
            //TODO: 200 응답이 아닐 때를 조금 더 세분화시키기 (https://github.com/kakao-tech-campus-2nd-step2/spring-gift-order/pull/267#discussion_r1692966327)
            throw new ServerException(ErrorCode.TOKEN_UNEXPECTED_ERROR);
        }
    }

    private OauthTokenResponse getKakaoOauthToken(String authorizationCode) {
        try {
            var body = new LinkedMultiValueMap<String, String>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", KAKAO_API_KEY);
            body.add("redirect_uri", KAKAO_REDIRECT_URI);
            body.add("code", authorizationCode);

            return restClient
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(OauthTokenResponse.class)
                .getBody();

        } catch (Exception e) {
            //TODO: 200 응답이 아닐 때를 조금 더 세분화시키기 (https://github.com/kakao-tech-campus-2nd-step2/spring-gift-order/pull/267#discussion_r1692966327)
            throw new ServerException(ErrorCode.TOKEN_UNEXPECTED_ERROR);
        }
    }
}