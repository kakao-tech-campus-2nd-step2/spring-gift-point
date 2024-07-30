package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.Kakao.*;
import gift.DTO.Order.OrderRequest;
import gift.DTO.Product.ProductResponse;
import gift.DTO.Token;
import gift.DTO.User.UserRequest;
import gift.DTO.User.UserResponse;
import gift.domain.User;
import gift.security.JwtTokenProvider;
import gift.security.KakaoTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.aot.generate.AccessControl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
public class KakaoService {
    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;
    private final KakaoTokenProvider kakaoTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final String loginUrl;
    private final String messageUrl;
    private final String userInfoUrl;

    public KakaoService(
            KakaoTokenProvider kakaoTokenProvider,
            JwtTokenProvider jwtTokenProvider,
            KakaoProperties kakaoProperties,
            UserService userService,
            @Value("${kakao.loginUrl}") String loginUrl,
            @Value("${kakao.messageUrl}") String messageUrl,
            @Value("${kakao.userInfoUrl}") String userInfoUrl
    ) {
        this.kakaoTokenProvider = kakaoTokenProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoProperties = kakaoProperties;
        this.userService = userService;
        this.loginUrl = loginUrl;
        this.messageUrl = messageUrl;
        this.userInfoUrl = userInfoUrl;
    }

    /*
     * 카카오 로그인 페이지를 연결하는 로직
     */
    public ResponseEntity<String> getCode(){
        return client.get()
                .uri(URI.create(loginUrl))
                .header(HttpHeaders.ACCEPT, "text.html")
                .retrieve()
                .toEntity(String.class);
    }
    /*
     * Code를 이용하여 트큰을 발급하는 로직
     */
    @Transactional
    public Token getKakaoToken(String code) throws JsonProcessingException {
        String token = kakaoTokenProvider.getToken(code);
        String kakaoId = getKakaoUserInfo(token);
        UserResponse kakaoUserResponse = userService.saveKakao(kakaoId, token);

        return jwtTokenProvider.makeToken(new UserRequest(
                kakaoUserResponse.getEmail(), kakaoUserResponse.getPassword()
        ));
    }
    /*
     * 상품 구매에 따른 동작 중 "나에게 메세지 보내기" 로직
     */
    public void messageToMe(
            String accessToken, ProductResponse productResponse, String optionName, OrderRequest orderRequest
    ) throws JsonProcessingException {
        LinkedMultiValueMap<Object, Object> body = makeBody(productResponse, optionName, orderRequest);
        client.post()
                .uri(URI.create(messageUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class);
    }
    /*
     * meesage Api 호출을 위한 요청의 body를 만들어주는 로직
     */
    private LinkedMultiValueMap<Object, Object> makeBody(
            ProductResponse productResponse, String optionName, OrderRequest orderRequest
    ) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();

        Link link = new Link("localhost:8080");

        String title = optionName + " x " + orderRequest.getQuantity() + "개\n" + orderRequest.getMessage();
        Content content = new Content(
                title,
                productResponse.getImageUrl(),
                "",
                link
        );
        Commerce commerce = new Commerce(productResponse.getName(), productResponse.getPrice());
        Template template = new Template("commerce", content, commerce);

        String template_str = objectMapper.writeValueAsString(template);

        LinkedMultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        body.set("template_object", template_str);
        return body;
    }
    /*
     * 토큰을 이용하여 유저의 정보를 받아오기
     */
    public String getKakaoUserInfo(String access_token) throws JsonProcessingException {
        ResponseEntity<String> entity = client.post()
                .uri(URI.create(userInfoUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + access_token)
                .retrieve()
                .toEntity(String.class);

        String resBody = entity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resBody);
        return jsonNode.get("id").asText();
    }
    /*
     * login을 위해 code를 받기 위한 url을 만드는 로직
     */
    public String makeLoginUrl(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(loginUrl);
        stringBuffer.append("?scope=talk_message");
        stringBuffer.append("&response_type=code");
        stringBuffer.append("&redirect_uri=").append(kakaoProperties.getRedirectUrl());
        stringBuffer.append("&client_id=").append(kakaoProperties.getClientId());

        return stringBuffer.toString();
    }
}
