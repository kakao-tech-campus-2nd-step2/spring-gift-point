package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.OrderResponseDto;
import gift.entity.Member;
import gift.exception.BusinessException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Autowired
    public KakaoService(RestTemplateBuilder builder, KakaoProperties kakaoProperties,
        MemberRepository memberRepository, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        restTemplate = builder.build();
        this.kakaoProperties = kakaoProperties;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = restTemplate.exchange(request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        return responseBody.get("access_token").toString();
    }

    public String getKakaoUserId(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", "Bearer " + accessToken);
        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        var response = restTemplate.exchange(request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        return responseBody.get("id").toString();
    }

    public String registerKakaoMember(String kakaoUserId, String token) {
        String email = kakaoUserId + "@kakao.com";
        String password = "password";
        Member member = new Member(email, password, token);
        memberRepository.save(member);
        return JwtUtil.generateToken(email);
    }

    public void sendKakaoMessage(Long memberId, OrderResponseDto order)
        throws JsonProcessingException {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException("카카오메세지를 보낼 대상이 없습니다."));

        String accessToken = member.getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            return;
        }

        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        var body = new LinkedMultiValueMap<String, String>();
        String template = objectMapper.writeValueAsString(Map.of(
            "object_type", "text",
            "text", order.getMessage(),
            "button_title", "선물 확인하기",
            "link", Map.of("web_url", "https://developers.kakao.com", "mobile_web_url",
                "https://developers.kakao.com")
        ));
        body.add("template_object", template);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = restTemplate.exchange(request,
            new ParameterizedTypeReference<Map<String, Object>>() {
            });
    }
}