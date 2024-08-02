package gift.service;

import gift.DTO.KakaoProfile;
import gift.DTO.KakaoProperties;
import gift.DTO.KakaoToken;
import gift.DTO.MemberDTO;
import gift.auth.DTO.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoLoginService {

    private static final String KAKAO_API_URL = "https://kapi.kakao.com";

    @Autowired
    private KakaoProperties kakaoProperties;

    @Autowired
    private MemberService memberService;

    private final RestClient restClient = RestClient.builder().build();

    public String getAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri="
                + kakaoProperties.redirectUrl() + "&client_id=" + kakaoProperties.restApiKey();
    }

    public KakaoToken getToken(String code) {
        System.out.println("code = " + code);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.restApiKey());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);

        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KakaoToken.class)
                .getBody();
    }

    public KakaoProfile getKakaoUserInfo(KakaoToken kakaoToken) {
        String accessToken = kakaoToken.access_token();

        String uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("kapi.kakao.com")
                .path("/v2/user/me")
//                .queryParam("property_keys", "[\"kakao_account.name\"]")
                .build()
                .toUriString();

        System.out.println("uri = " + uri);

        return restClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(KakaoProfile.class)
                .getBody();
    }

    public TokenDTO createToken(KakaoToken kakaoToken) {
        String email = getKakaoUserInfo(kakaoToken).id() + "@kakao.com";
        var member = new MemberDTO(email, "kakao", kakaoToken.access_token());
        if (!memberService.isExist(member)) {
            return memberService.signUp(member, kakaoToken.expires_in());
        }
        return memberService.login(member, kakaoToken.expires_in());
    }
}
