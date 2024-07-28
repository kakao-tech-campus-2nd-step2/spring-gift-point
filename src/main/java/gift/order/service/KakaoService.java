package gift.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import gift.common.config.KakaoProperties;
import gift.option.domain.OrderResponse;
import gift.order.domain.Token;
import gift.order.dto.KakaoUser;
import gift.order.dto.KakaoTokenResponse;
import gift.order.dto.Link;
import gift.order.dto.TemplateObject;
import gift.order.repository.TokenJPARepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;

@Service
public class KakaoService {
    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().requestFactory(getClientHttpRequestFactory()).build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenJPARepository tokenJPARepository;
    public KakaoService(KakaoProperties kakaoProperties, TokenJPARepository tokenJPARepository) {
        this.kakaoProperties = kakaoProperties;
        this.tokenJPARepository = tokenJPARepository;
    }

    // RestClinet Timeout 설정
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(5000); // 서버에 연결하는 데 걸리는 최대 시간이 5000ms(5초)
        clientHttpRequestFactory.setConnectionRequestTimeout(5000); // 커넥션 풀에서 커넥션을 얻는데 걸리는 최대 시간이 5000ms(5초)
        return clientHttpRequestFactory;
    }

    // accessToken과 refreshToken을 받아와 저장하기
    @Transactional
    public ResponseEntity<KakaoTokenResponse> getToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        // body
        LinkedMultiValueMap<String, String> body = createBody(code);

        // RestClient 사용하여 POST 요청
        KakaoTokenResponse kakaoTokenResponse = restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KakaoTokenResponse.class)
                .getBody();

        return ResponseEntity.ok(kakaoTokenResponse);
    }

    // RestClient의 body를 만들기
    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", kakaoProperties.grantType()); // authorization_code로 고정
        body.add("client_id", kakaoProperties.clientId()); // REST API 키
        body.add("redirect_uri", kakaoProperties.redirectUri()); // 인가 코드가 리다이렉트된 URI
        body.add("code", code); // 인가 코드

        return body;
    }

    // accesstoken을 활용하여 사용자 정보 받아와 저장하기
    @Transactional
    public KakaoUser getUserInfo(String accessToken) throws IOException {
        String url = "https://kapi.kakao.com/v2/user/me";

        // RestClient 사용하여 GET 요청
        String response = restClient.get()
                .uri(URI.create(url))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        // KakaoMember
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode kakaoAccount = jsonNode.get("kakao_account");
        Long id = jsonNode.get("id").asLong();
        String nickname = kakaoAccount.get("profile").get("nickname").asText();

        return new KakaoUser(id, nickname);
    }

    // 토큰값과 사용자 정보 저장하기
    @Transactional
    public void saveToken(KakaoTokenResponse tokenResponse, KakaoUser kakaoUser) {
        String accessToken = tokenResponse.accessToken();
        String refreshToken = tokenResponse.refreshToken();
        String userName = kakaoUser.nickname();
        Long expiresIn = tokenResponse.expiresIn();
        Token token = new Token(accessToken, refreshToken, userName, expiresIn);

        tokenJPARepository.save(token);
    }

    public void sendKakaoMessage(OrderResponse orderResponse, String accessToken) throws JsonProcessingException {
        // validate
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 액세스 토큰입니다.");
        }

        // 카카오톡 메시지 보내기
        ObjectMapper objectMapper = new ObjectMapper();
        TemplateObject templateObject = new TemplateObject(
                "text",
                orderResponse.toString(),
                new Link("https://developers.kakao.com")
        );
        String templateObjectToJson = objectMapper.writeValueAsString(templateObject);
        MultiValueMap<String, Object> body_map = new LinkedMultiValueMap<>();
        body_map.set("template_object", templateObjectToJson);

        restClient.post()
                .uri(kakaoProperties.getMessageToMeUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .body(body_map)
                .retrieve()
                .body(String.class);
    }

    // 토큰이 유효한지 검증하는 로직
    public boolean validateToken(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/access_token_info";

        // RestClient 사용하여 GET 요청
        String response = restClient.get()
                .uri(URI.create(url))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        // 토큰이 유효한지 확인
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonNode != null;
    }

    // 토큰값 갱신하기
    @Transactional
    public String renewToken(String accessToken) {
        String refreshToken = tokenJPARepository.findByAccessToken(accessToken).getRefreshToken();
        String url = "https://kauth.kakao.com/oauth/token";

        // RestClient 사용하여 POST 요청하여 토큰값 갱신
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoProperties.clientId());
        body.add("refresh_token", refreshToken);

        KakaoTokenResponse tokenResponse = restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KakaoTokenResponse.class)
                .getBody();
        if (tokenResponse == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 액세스 토큰입니다.");
        }

        // 토큰값 update
        Token token = tokenJPARepository.findByAccessToken(accessToken);
        token.setAccessToken(tokenResponse.accessToken());
        token.setRefreshToken(refreshToken);
        tokenJPARepository.save(token);

        return tokenResponse.accessToken();
    }
}
