package gift.member.service;

import gift.member.dto.MemberResponse;
import gift.member.dto.KakaoProfileDto;
import gift.member.dto.KakaoTokenDto;
import gift.member.dto.MemberDto;
import gift.member.exception.InvalidKakaoTokenException;
import gift.member.repository.KakaoTokenRepository;
import gift.global.util.JwtProvider;
import gift.member.KakaoProperties;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoLoginService {

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final KakaoTokenRepository kakaoTokenRepository;

    private static final String KAKAO_OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_PROFILE_URL = "https://kapi.kakao.com/v2/user/me";

    @Autowired
    public KakaoLoginService(RestTemplateBuilder restTemplateBuilder, KakaoProperties kakaoProperties,
        MemberService memberService, JwtProvider jwtProvider, KakaoTokenRepository kakaoTokenRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.kakaoProperties = kakaoProperties;
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public MemberResponse login(String code) {
        var kakaoTokenDto = getKakaoToken(code);
        var kakaoProfileDto = getKakaoProfile(kakaoTokenDto.access_token());

        String email = kakaoProfileDto.kakao_account().email();
        MemberDto foundMemberDto = memberService.findMember(email);
        kakaoTokenRepository.save(kakaoTokenDto.toEntity(email));
        return new MemberResponse(jwtProvider.createAccessToken(foundMemberDto));
    }

    private KakaoTokenDto getKakaoToken(String code) {
        var client = RestClient.builder(restTemplate).build();
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);
        try {
            return client.post()
                .uri(URI.create(KAKAO_OAUTH_TOKEN_URL))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(KakaoTokenDto.class);
        } catch (HttpClientErrorException e) {
            throw new InvalidKakaoTokenException(e.getStatusCode(), e.getMessage());
        }
    }

    private KakaoProfileDto getKakaoProfile(String kakaoAccessToken) {
        var client = RestClient.builder(restTemplate).build();
        try {
            return client.get()
                .uri(URI.create(KAKAO_PROFILE_URL))
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .body(KakaoProfileDto.class);
        } catch (HttpClientErrorException e) {
            throw new InvalidKakaoTokenException(e.getStatusCode(), e.getMessage());
        }
    }
}
