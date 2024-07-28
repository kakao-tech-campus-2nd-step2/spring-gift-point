package gift.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.domain.AuthInfo;
import gift.auth.domain.KakaoProperties;
import gift.auth.dto.KakaoMemberResponseDto;
import gift.auth.dto.KakaoTokenResponseDto;
import gift.auth.dto.LoginRequestDto;
import gift.auth.exception.KakaoTokenException;
import gift.global.security.TokenManager;
import gift.member.domain.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;
    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private final ObjectMapper objectMapper;

    public static final String BEARER_TYPE = "Bearer ";
    private static final String AUTHORIZE_URL_FORMAT = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s";
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String GET_MEMBER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final List<String> PROPERTY_KEYS = List.of("kakao_account.profile");

    public AuthService(MemberRepository memberRepository, TokenManager tokenManager, RestTemplate restTemplate, KakaoProperties kakaoProperties, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.tokenManager = tokenManager;
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;
    }

    public HttpHeaders login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmailAndPassword(loginRequestDto.email(), loginRequestDto.password())
                .orElseThrow(MemberNotFoundException::new);

        return getAuthorizationHeader(member);
    }

    public String getKakaoAuthUrl() {
        return String.format(AUTHORIZE_URL_FORMAT, kakaoProperties.getClientId(), kakaoProperties.getRedirectUri());
    }

    public HttpHeaders kakaoLogin(String code) throws JsonProcessingException {
        String accessToken = getKakaoAccessToken(code);
        Member member = getKakaoMember(accessToken);

        return getAuthorizationHeader(member);
    }

    private HttpHeaders getAuthorizationHeader(Member member) {
        String accessToken = BEARER_TYPE + tokenManager.createAccessToken(new AuthInfo(member));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return headers;
    }

    private String getKakaoAccessToken(String code) {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUri());
        body.add("code", code);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(TOKEN_URL));

        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(request, KakaoTokenResponseDto.class);

        if (response.getStatusCode() != HttpStatus.OK || Objects.isNull(response.getBody())) {
            throw new KakaoTokenException();
        }

        return response.getBody().accessToken();
    }

    private Member getKakaoMember(String accessToken) throws JsonProcessingException {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        URI uri = UriComponentsBuilder.fromHttpUrl(GET_MEMBER_INFO_URL)
                .queryParam("property_keys", objectMapper.writeValueAsString(PROPERTY_KEYS))
                .build()
                .toUri();

        var request = new RequestEntity<>(headers, HttpMethod.POST, uri);

        ResponseEntity<KakaoMemberResponseDto> response = restTemplate.exchange(request, KakaoMemberResponseDto.class);

        if (response.getStatusCode() != HttpStatus.OK || Objects.isNull(response.getBody())) {
            throw new KakaoTokenException();
        }

        Member responseMember = response.getBody().toMember();

        Member member = memberRepository.findByEmail(responseMember.getEmail())
                .orElseGet(() -> memberRepository.save(responseMember));

        member.updateAccessToken(accessToken);
        memberRepository.save(member);

        // Member를 이메일로 조회하고, 없으면 responseMember를 저장
        return member;
    }
}
