package gift.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.dto.LoginMemberToken;
import gift.repository.JpaMemberRepository;
import gift.exceptionAdvisor.exceptions.GiftUnauthorizedException;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class KakaoLoginService {

    @Value("${kakao.api_key}")
    private String apiKey;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    private final JpaMemberRepository jpaMemberRepository;

    private final AuthenticationTool authenticationTool;

    public KakaoLoginService(JpaMemberRepository jpaMemberRepository,
        AuthenticationTool authenticationTool) {
        this.jpaMemberRepository = jpaMemberRepository;
        this.authenticationTool = authenticationTool;
    }

    public String getConnectionUrl() {
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri="
            + redirectUri + "&response_type=code";
    }

    public String getToken(String code) {
        WebClient webClient = WebClient.create();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        return webClient.post().uri("https://kauth.kakao.com/oauth")
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .body(BodyInserters.fromValue(body)).retrieve().bodyToMono(String.class).block();
    }

    public LoginMemberToken getKakaoUser(String token) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(token);
            String accessToken = jsonNode.get("access_token").asText();
            return new LoginMemberToken(accessToken);

        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(System.out::println);
            System.out.println(e.getMessage());
            throw new GiftUnauthorizedException("카카오 로그인에 실패하였습니다.");
        }

    }
}
