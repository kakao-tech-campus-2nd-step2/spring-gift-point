package gift.service;

import gift.dto.JwtResponse;
import gift.dto.KakaoProfileDTO;
import gift.dto.KakaoTokenDTO;
import gift.dto.MemberDTO;
import gift.exception.InvalidKakaoTokenException;
import gift.repository.KakaoTokenRepository;
import gift.util.JwtProvider;
import gift.util.KakaoProperties;
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

    public JwtResponse login(String code) {
        var kakaoTokenDTO = getKakaoToken(code);
        var kakaoProfileDTO = getKakaoProfile(kakaoTokenDTO.access_token());

        String email = kakaoProfileDTO.kakao_account().email();
        MemberDTO foundMemberDTO = memberService.findMember(email);
        kakaoTokenRepository.save(kakaoTokenDTO.toEntity(email));
        return new JwtResponse(jwtProvider.createAccessToken(foundMemberDTO));
    }

    private KakaoTokenDTO getKakaoToken(String code) {
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
                .body(KakaoTokenDTO.class);
        } catch (HttpClientErrorException e) {
            throw new InvalidKakaoTokenException(e.getStatusCode(), e.getMessage());
        }
    }

    private KakaoProfileDTO getKakaoProfile(String kakaoAccessToken) {
        var client = RestClient.builder(restTemplate).build();
        try {
            return client.get()
                .uri(URI.create(KAKAO_PROFILE_URL))
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .body(KakaoProfileDTO.class);
        } catch (HttpClientErrorException e) {
            throw new InvalidKakaoTokenException(e.getStatusCode(), e.getMessage());
        }
    }
}
