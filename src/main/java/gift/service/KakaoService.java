package gift.service;

import gift.dto.KakaoTokenDto;
import gift.dto.MemberDto;
import gift.dto.response.TokenResponse;
import gift.exception.MemberNotFoundException;
import gift.repository.KakaoTokenRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class KakaoService {

    private final MemberService memberService;
    private final RestTemplate restTemplate;
    private final KakaoTokenService kakaoTokenService;
    private final KakaoTokenRepository kakaoTokenRepository;
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-url}")
    private String redirectUrl;
    @Value("${kakao.url}")
    private String url;

    @Autowired
    public KakaoService(MemberService memberService, RestTemplate restTemplate, KakaoTokenService kakaoTokenService, KakaoTokenRepository kakaoTokenRepository) {
        this.memberService = memberService;
        this.restTemplate = restTemplate;
        this.kakaoTokenService = kakaoTokenService;
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    private TokenResponse getKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUrl);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                return new TokenResponse(jsonResponse.getString("access_token"), jsonResponse.getString("refresh_token"));
            } else {
                System.err.println("Failed to get Kakao token: " + response.getStatusCode() + " " + response.getBody());
                throw new RuntimeException("Failed to get Kakao token: " + response.getStatusCode() + " " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Exception while getting Kakao token: " + e.getMessage());
            throw new RuntimeException("Exception while getting Kakao token", e);
        }
    }


    public JSONObject getUserInfo(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(reqURL, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return new JSONObject(response.getBody());
        }

        throw new RuntimeException("Failed to get user info from Kakao");
    }

    public String login(String code) throws MemberNotFoundException {
        TokenResponse token = getKakaoToken(code);
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();
        JSONObject userInfo = getUserInfo(accessToken);

        String email = null;
        if (userInfo.has("kakao_account")) {
            JSONObject kakaoAccount = userInfo.getJSONObject("kakao_account");
            if (kakaoAccount.has("email")) {
                email = kakaoAccount.getString("email");
            }
        }
        if (email == null) {
            throw new MemberNotFoundException("Email not found");
        }

        try {
            memberService.getMember(email);
        } catch (MemberNotFoundException e) {
            KakaoTokenDto kakaoTokenDto = new KakaoTokenDto(email, accessToken, refreshToken);
            register(email, kakaoTokenDto);
        }
        return accessToken;
    }

    @Transactional
    public void register(String email, KakaoTokenDto kakaoTokenDto) {
        MemberDto memberDto = new MemberDto(email, email + "kakao");
        memberService.registerMember(memberDto);
        memberService.login(email, email + "kakao");
        kakaoTokenService.saveToken(kakaoTokenDto);

    }

    @Transactional
    public void sendKakaoMessage(String accessToken, String message) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("template_object", new JSONObject()
                .put("object_type", "text")
                .put("text", message)
                .put("link", new JSONObject()
                        .put("web_url", "https://gift.kakao.com/home")
                        .put("mobile_web_url", "https://gift.kakao.com/home"))
                .put("button_title", "자세히 보기")
                .toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to send Kakao message: " + response.getStatusCode() + " " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Exception while sending Kakao message: " + e.getMessage());
            throw new RuntimeException("Exception while sending Kakao message", e);
        }
    }
}
