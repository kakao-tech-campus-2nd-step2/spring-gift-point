package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.config.KakaoProperties;
import gift.main.dto.*;
import gift.main.entity.ApiToken;
import gift.main.handler.TextTemplateFactory;
import gift.main.repository.ApiTokenRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Objects;

import static io.jsonwebtoken.lang.Strings.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class KakaoService {

    private static final MediaType FORM_URLENCODED = new MediaType(APPLICATION_FORM_URLENCODED, UTF_8);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String TEMPLATE_OBJECT = "template_object";

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final ApiTokenRepository apiTokenRepository;

    public KakaoService(KakaoProperties kakaoProperties, ApiTokenRepository apiTokenRepository) {
        this.kakaoProperties = kakaoProperties;
        this.apiTokenRepository = apiTokenRepository;
        restClient = RestClient.create();
    }

    //카카오 인가코드를 이용한 엑세스 토큰 요청하기
    public KakaoToken requestKakaoToken(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", kakaoProperties.authorizationCode());
        map.add("client_id", kakaoProperties.clientId());
        map.add("redirect_uri", kakaoProperties.redirectUri());
        map.add("code", code);

        KakaoToken kakaoToken= restClient.post()
                .uri(kakaoProperties.tokenRequestUri())
                .contentType(FORM_URLENCODED)
                .body(map)
        .exchange((request, response) -> {
            System.out.println("response.getClass() = " + response.getClass());
            System.out.println("response = " + response);
            KakaoToken token = new KakaoToken(Objects.requireNonNull(response.bodyTo(TempToken.class)),LocalDateTime.now());
                return token;
        });
        System.out.println("kakaoToken = " + kakaoToken);
        return kakaoToken;
    }

    //카카오 엑세스 토큰을 이용한 유저정보 가져오기
    public UserJoinRequest getKakaoProfile(KakaoToken tokenResponse) {
        KakaoProfileRequest kakaoProfileRequest = restClient.post()
                .uri(kakaoProperties.userRequestUri())
                .contentType(FORM_URLENCODED)
                .header(AUTHORIZATION, BEARER + tokenResponse.accessToken())
                .retrieve()
                .toEntity(KakaoProfileRequest.class)
                .getBody();

        System.out.println("kakaoProfileRequest = " + kakaoProfileRequest);
        return new UserJoinRequest(kakaoProfileRequest);

    }

    public void sendOrderMessage(OrderResponce orderResponce, UserVo userVo) {
        //요청바디 객체를 만드는 부분
        MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
        String templateObjectJson = TextTemplateFactory.convertOrderResponseToTextTemplateJson(orderResponce);
        map.set(TEMPLATE_OBJECT, templateObjectJson);

        //요청을 위한 토큰을 가지고 오는 로직
        ApiToken apiToken = apiTokenRepository.findByUserId(userVo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TOKEN));

        restClient.post()
                .uri(kakaoProperties.messageRequestUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(AUTHORIZATION, BEARER + apiToken.getAccessToken())
                .body(map)
                .retrieve()
                .toEntity(String.class);
    }

}
