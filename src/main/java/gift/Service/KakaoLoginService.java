package gift.Service;

import gift.DTO.ResponseKaKaoUserInfo;
import gift.DTO.ResponseKakaoTokenDTO;
import gift.Exception.KaKaoBadRequestException;
import gift.Exception.KaKaoServerErrorException;
import gift.Model.Entity.Member;
import gift.Model.Value.Email;
import gift.Model.Value.Password;
import gift.Repository.MemberRepository;
import gift.Util.JwtUtil;
import gift.Util.KakaoProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class KakaoLoginService {
    private final RestClient client;
    private final KakaoProperties properties;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtill;

    public KakaoLoginService(RestClient client, KakaoProperties properties, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.client = client;
        this.properties = properties;
        this.memberRepository = memberRepository;
        this.jwtUtill=jwtUtil;
    }

    public URI requestLogin() {
        String url = generateLoginUrl();
        URI redirectionUri = client.get()
                .uri(URI.create(url))
                .retrieve()
                .toEntity(String.class)
                .getHeaders()
                .getLocation();

        return redirectionUri;
    }

    private String generateLoginUrl() {
       return UriComponentsBuilder.fromUriString(properties.loginUri())
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUrl())
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }

    @Transactional
    public String loginOrRegisterUser(String oauthCode) {
        String kakaoAccessToken = getToken(oauthCode);
        String userEmail;
        userEmail = client.post()
                .uri(URI.create(properties.getUserInfoUri()))
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+kakaoAccessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new KaKaoBadRequestException("카카오 정보 가져오기 API : " + response.getStatusCode() + "에러 발생. ");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new KaKaoServerErrorException("카카오 정보 가져오기 API : " + response.getStatusCode() + "에러 발생. ");
                })
                .toEntity(ResponseKaKaoUserInfo.class)
                .getBody()
                .getKakaoAccount()
                .getEmail();

        Email email = new Email(userEmail);
        Member member = memberRepository.findByEmail(email).orElseGet(()->memberRepository.save(new Member(email, new Password("카카오 유저"))));
        member.updateAccessToken(kakaoAccessToken);
        return jwtUtill.generateToken(member);
    }

    private String getToken(String oauthCode){
        String uri = properties.generateTokenUri();
        LinkedMultiValueMap<String, String> body = generateBodyForKakaoToken(oauthCode);
        String accessToken = client.post()
                .uri(URI.create(uri))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new KaKaoBadRequestException("카카오 토큰 가져오기 API : " + response.getStatusCode() + "에러 발생. ");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new KaKaoServerErrorException("카카오 토큰 가져오기 API : " + response.getStatusCode() + "에러 발생. ");
                })
                .toEntity(ResponseKakaoTokenDTO.class)
                .getBody()
                .getAccessToken();
        return accessToken;
    }

    private LinkedMultiValueMap<String, String> generateBodyForKakaoToken(String oauthCode) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.redirectUrl());
        body.add("code", oauthCode);

        return body;
    }
}
