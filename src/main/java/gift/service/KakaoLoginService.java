package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import gift.config.JwtProvider;
import gift.domain.member.Member;
import gift.domain.member.SocialAccount;
import gift.domain.member.SocialType;
import gift.exception.ErrorCode;
import gift.exception.GiftException;
import gift.repository.MemberRepository;
import gift.service.kakao.KakaoApiService;
import gift.service.kakao.KakaoTokenInfoResponse;
import gift.service.kakao.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class KakaoLoginService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final KakaoApiService kakaoApiService;

    public KakaoLoginService(MemberRepository memberRepository, JwtProvider jwtProvider, KakaoApiService kakaoApi) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.kakaoApiService = kakaoApi;
    }

    public HttpHeaders getRedirectHeaders() {
        String url = kakaoApiService.createLoginUrl();
        return createRedirectHeaders(url);
    }

    public TokenResponse processKakaoAuth(String code) {
        KakaoTokenInfoResponse tokenInfo = kakaoApiService.getTokenInfo(code);

        String accessToken = tokenInfo.getAccessToken();
        String refreshToken = tokenInfo.getRefreshToken();

        Member kakaoMember = getKakaoMember(accessToken, refreshToken);
        String jwt = jwtProvider.create(kakaoMember);

        return new TokenResponse(jwt);
    }

    private Member getKakaoMember(String accessToken, String refreshToken) {
        ResponseEntity<String> response = kakaoApiService.getUserInfo(accessToken);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GiftException(ErrorCode.KAKAO_USER_RETRIEVAL_FAILED);
        }

        JsonNode jsonNode = kakaoApiService.parseJson(response.getBody());
        Long kakaoId = jsonNode.get("id").asLong();

        return memberRepository.findBySocialAccount_SocialIdAndSocialAccount_SocialType(kakaoId, SocialType.KAKAO)
                .map(member -> {
                    SocialAccount socialAccount = member.getSocialAccount();
                    socialAccount.changeAccessToken(accessToken);
                    socialAccount.changeRefreshToken(refreshToken);
                    return member;
                })
                .orElseGet(() -> createAndSaveMember(kakaoId, accessToken, refreshToken));
    }

    private Member createAndSaveMember(Long kakaoId, String accessToken, String refreshToken) {
        Member member = new Member.MemberBuilder()
                .build();

        SocialAccount socialAccount = new SocialAccount(SocialType.KAKAO, kakaoId, accessToken, refreshToken);
        member.setSocialAccount(socialAccount);

        return memberRepository.save(member);
    }

    private HttpHeaders createRedirectHeaders(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return headers;
    }

}
