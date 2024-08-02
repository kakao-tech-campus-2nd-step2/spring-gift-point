package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.OAuthToken;
import gift.dto.KakaoResponse;
import gift.dto.OAuthTokenDto;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OauthService {
    @Value("${kakao.rest-api-key}")
    private String restApiKey;

    private final MemberService memberService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OauthService(MemberService memberService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void redirectKakaoLogin(HttpServletResponse response) throws IOException {
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?scope=talk_message,account_email&response_type=code&redirect_uri=http://localhost:8080&client_id=" + restApiKey;
        response.sendRedirect(kakaoUrl);
    }

    public String handleKakaoCallback(String code) throws JsonProcessingException {
        // header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", restApiKey);
        params.add("redirect_uri", "http://localhost:8080");
        params.add("code", code);
        // header + body
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // POST 방식으로 Http 요청, 응답 저장
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        return oAuthToken.getAccess_token();
    }

    public void registerUser(OAuthTokenDto oAuthTokenDto) throws JsonProcessingException {
        String accessToken = oAuthTokenDto.getAccessToken();
        String password = oAuthTokenDto.getPassword();

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // Body
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("property_keys", "[\"kakao_account.email\"]");

        // header + body 합치기
        HttpEntity<MultiValueMap<String, Object>> kakaoProfileRequest = new HttpEntity<>(params, headers);

        // POST로 http 요청을 보내고 응답을 받는다.
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoProfileRequest,
            String.class
        );

        KakaoResponse kakaoResponse = objectMapper.readValue(response.getBody(), KakaoResponse.class);
        String email = kakaoResponse.getKakao_account().getEmail();
        memberService.oauthSave(email, password, accessToken);
    }
}
