package gift.service;

import gift.config.KakaoProperties;
import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.dto.KakaoToken;
import gift.dto.KakaoUser;
import gift.dto.TokenResponseDto;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
public class KakaoOAuthService {
    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO = "https://kapi.kakao.com/v2/user/me";
    private final MemberRepository memberRepository;
    private final KakaoProperties kakaoProperties;
    private final JwtUtil jwtUtil;
    private final RestClient restClient;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public KakaoOAuthService(MemberRepository memberRepository, KakaoProperties kakaoProperties, JwtUtil jwtUtil, RestClient.Builder builder) {
        this.memberRepository = memberRepository;
        this.kakaoProperties = kakaoProperties;
        this.jwtUtil = jwtUtil;
        this.restClient = builder.build();
    }

    public KakaoToken getKakaoToken(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectURL());
        body.add("code", code);
        var response = restClient.post()
                .uri(URI.create(KAKAO_TOKEN_URI))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Error Response Body: {}", (res.getBody()));
                    throw new CustomException(ErrorCode.UNAUTHORIZED_KAKAO);
                })
                .toEntity(KakaoToken.class);
        log.info(response.toString());
        return new KakaoToken(response.getBody().accessToken(), response.getBody().refreshToken());
    }

    public TokenResponseDto kakaoMemberRegister(KakaoToken token) {
        var response = restClient.post()
                .uri(URI.create(KAKAO_USER_INFO))
                .header("Authorization", "Bearer " + token.accessToken())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Error Response Body: {}", (res.getBody()));
                    throw new IllegalArgumentException("사용자 정보 조회 오류");
                })
                .toEntity(KakaoUser.class);
        log.info(response.toString());
        Member member = registerMember(response.getBody(), token);
        String jwtToken = jwtUtil.generateToken(member.getId(), member.getEmail());
        return new TokenResponseDto(jwtToken);
    }

    private Member registerMember(KakaoUser memberInfo, KakaoToken token) {
        String email = memberInfo.kakaoAccount().email();
        String name = memberInfo.kakaoAccount().profile().nickName();
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            Member signupMember = new Member(email, name, UUID.randomUUID().toString(),
                    token.accessToken(), token.refreshToken(), 1);
            memberRepository.save(signupMember);
            return signupMember;
        }
        return member.get();
    }

    public void unlink(String token) {
        String logoutUri = "https://kapi.kakao.com/v1/user/unlink";
        var response = restClient.post()
                .uri(URI.create(logoutUri))
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .toEntity(String.class);
        log.info(response.getBody());
    }
}


