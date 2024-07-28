package gift.service.oauth;

import static gift.util.constants.auth.KakaoOAuthConstants.TOKEN_NOT_FOUND;

import gift.client.KakaoApiClient;
import gift.dto.member.MemberRegisterRequest;
import gift.dto.member.MemberResponse;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import gift.exception.member.EmailAlreadyUsedException;
import gift.exception.oauth.KakaoTokenNotFoundException;
import gift.model.RegisterType;
import gift.model.oauth.KakaoToken;
import gift.repository.TokenRepository;
import gift.service.MemberService;
import gift.util.JWTUtil;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoOAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final MemberService memberService;
    private final TokenRepository tokenRepository;
    private final JWTUtil jwtUtil;
    private final RestClient restClient;

    public KakaoOAuthService(
        KakaoApiClient kakaoApiClient,
        MemberService memberService,
        TokenRepository tokenRepository,
        JWTUtil jwtUtil,
        RestClient restClient
    ) {
        this.kakaoApiClient = kakaoApiClient;
        this.memberService = memberService;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
        this.restClient = restClient;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        return kakaoApiClient.getAccessToken(code);
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        return kakaoApiClient.getUserInfo(accessToken);
    }

    public KakaoUnlinkResponse unlinkUser(Long memberId) {
        KakaoToken kakaoToken = tokenRepository.findByMemberId(memberId)
            .orElseThrow(() -> new KakaoTokenNotFoundException(TOKEN_NOT_FOUND));
        return kakaoApiClient.unlinkUser(kakaoToken.getAccessToken());
    }

    public KakaoScopeResponse getUserScopes(Long memberId) {
        KakaoToken kakaoToken = tokenRepository.findByMemberId(memberId)
            .orElseThrow(() -> new KakaoTokenNotFoundException(TOKEN_NOT_FOUND));
        return kakaoApiClient.getUserScopes(kakaoToken.getAccessToken());
    }

    public KakaoUserResponse getUserInfo(Long memberId) {
        KakaoToken kakaoToken = tokenRepository.findByMemberId(memberId)
            .orElseThrow(() -> new KakaoTokenNotFoundException(TOKEN_NOT_FOUND));
        return kakaoApiClient.getUserInfo(kakaoToken.getAccessToken());
    }

    @Transactional
    public MemberResponse registerOrLoginKakaoUser(KakaoUserResponse userResponse) {
        try {
            MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                userResponse.email(),
                "KAKAO_LOGIN",
                RegisterType.KAKAO
            );
            return memberService.registerMember(registerRequest);
        } catch (EmailAlreadyUsedException e) {
            return memberService.loginKakaoMember(userResponse.email());
        }
    }

    @Transactional
    public void saveToken(KakaoTokenResponse tokenResponse, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        KakaoToken kakaoToken = new KakaoToken(
            memberId,
            tokenResponse.accessToken(),
            tokenResponse.refreshToken(),
            now.plusSeconds(tokenResponse.expiresIn()),
            now.plusSeconds(tokenResponse.refreshTokenExpiresIn())
        );
        tokenRepository.save(kakaoToken);
    }

    public String generateJwt(Long memberId, String email) {
        return jwtUtil.generateToken(memberId, email);
    }

    public void sendMessage(Long memberId, String templateObject) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("template_object", templateObject);

        KakaoToken kakaoToken = tokenRepository.findByMemberId(memberId)
            .orElseThrow(() -> new KakaoTokenNotFoundException(TOKEN_NOT_FOUND));

        restClient.post()
            .uri(url)
            .headers(headers -> {
                headers.setBearerAuth(kakaoToken.getAccessToken());
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            })
            .body(requestBody)
            .retrieve()
            .toBodilessEntity();
    }
}
