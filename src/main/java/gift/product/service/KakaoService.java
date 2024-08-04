package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.INVALID_HTTP_REQUEST;
import static gift.product.exception.GlobalExceptionHandler.NOT_RECEIVE_RESPONSE;

import gift.product.dto.TokenDTO;
import gift.product.exception.RequestException;
import gift.product.exception.ResponseException;
import gift.product.model.Member;
import gift.product.model.SnsMember;
import gift.product.repository.MemberRepository;
import gift.product.repository.SnsMemberRepository;
import gift.product.util.JwtUtil;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final KakaoProperties properties;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final SnsMemberRepository snsMemberRepository;
    private final RestClient client = RestClient.builder()
        .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
            throw new RequestException(INVALID_HTTP_REQUEST);
        })
        .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
            throw new ResponseException(NOT_RECEIVE_RESPONSE);
        })
        .build();

    public KakaoService(
        KakaoProperties properties,
        MemberRepository memberRepository,
        JwtUtil jwtUtil,
        SnsMemberRepository snsMemberRepository
    ) {
        this.properties = properties;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.snsMemberRepository = snsMemberRepository;
    }

    public TokenDTO login(String authCode) {
        String accessToken = getAccessToken(authCode);
        Long kakaoMemberId = parsingAccessToken(accessToken);
        SnsMember snsMember = signUpAndLogin(kakaoMemberId, accessToken);
        return new TokenDTO(jwtUtil.generateToken(snsMember.getKakaoId().toString()));
    }

    public String getAuthCode() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
            + "&redirect_uri=" + properties.redirectUrl()
            + "&client_id=" + properties.clientId();
    }

    private String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody(code);
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(Map.class);
        if (response == null)
            throw new ResponseException(NOT_RECEIVE_RESPONSE);
        return response.get("access_token").toString();
    }

    private Long parsingAccessToken(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var response = client.get()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(Map.class);
        if (response == null)
            throw new ResponseException(NOT_RECEIVE_RESPONSE);
        return (Long) response.get("id");
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }

    private SnsMember signUpAndLogin(Long kakaoMemberId, String accessToken) {
        Optional<SnsMember> existMember = snsMemberRepository.findByKakaoId(kakaoMemberId);
        if(existMember.isEmpty()) {
            SnsMember newMember = snsMemberRepository.save(
                new SnsMember(
                    kakaoMemberId,
                    accessToken,
                    "Kakao"
                )
            );
            memberRepository.save(new Member(newMember));
            return newMember;
        }
        SnsMember snsMember = existMember.get();
        snsMember.setAccessToken(accessToken);
        return snsMemberRepository.save(snsMember);
    }
}