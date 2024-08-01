package gift.service.kakaoAuth;

import gift.domain.member.Member;
import gift.domain.oAuthToken.OAuthToken;
import gift.domain.oAuthToken.OAuthTokenRepository;
import gift.service.member.MemberService;
import gift.web.dto.MemberDto;
import gift.web.dto.Token;
import java.net.URI;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAuthService {

    private final OAuthTokenRepository oAuthTokenRepository;
    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final MemberService memberService;

    public KakaoAuthService(RestClient restClient, KakaoProperties kakaoProperties,
        MemberService memberService, OAuthTokenRepository oAuthTokenRepository) {
        this.restClient = restClient;
        this.kakaoProperties = kakaoProperties;
        this.memberService = memberService;
        this.oAuthTokenRepository = oAuthTokenRepository;
    }

    public void createMemberAndToken(MemberDto memberDto, String token) {
        memberService.createMember(memberDto);
        Member member = memberService.getMemberEntityByEmail(memberDto.email());

        oAuthTokenRepository.save(new OAuthToken(member, token));
    }

    public boolean isSignedUp(KakaoInfo kakaoInfo) {
        return memberService.existsByEmail(kakaoInfo.email());
    }

    public MemberDto getMemberInfo(KakaoInfo kakaoInfo) {
        return memberService.getMemberByEmail(kakaoInfo.email());
    }

    public String getKakaoAuthUrl() {
        StringBuffer str = new StringBuffer();
        str.append(kakaoProperties.authSettingUrl());
        str.append("&redirect_uri=" + kakaoProperties.redirectUri());
        str.append("&client_id=" + kakaoProperties.clientId());

        return str.toString();
    }

    public Token receiveToken(String code) {
        var body = kakaoProperties.createBody(code);

        var response = restClient.post()
            .uri(URI.create(kakaoProperties.tokenPostUrl()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(Map.class);

        return new Token(response.getBody().get("access_token").toString());
    }

    public KakaoInfo getMemberInfoFromKakaoServer(Token accessToken) {

        ResponseEntity<Map<String, Object>> response = restClient.post()
            .uri(URI.create(kakaoProperties.memberInfoPostUrl()))
            .header("Authorization", "Bearer " + accessToken.token())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, String> responseBody = (Map<String, String>) response.getBody().get("kakao_account");

        return new KakaoInfo(responseBody.get("email"));
    }
}
