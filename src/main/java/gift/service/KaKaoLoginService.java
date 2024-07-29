package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.Member;
import gift.dto.KakaoTokenInfo;
import gift.dto.KakaoUserInfo;
import gift.dto.response.AuthResponse;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Optional;
import java.util.Random;

import static gift.exception.ErrorCode.ALREADY_REGISTERED_ERROR;
import static gift.exception.ErrorCode.KAKAO_LOGIN_FAILED_ERROR;

@Service
public class KaKaoLoginService {

    @Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirectUrl}")
    private String redirectUrl;

    @Value("${kakao.grant-type}")
    private String grantType;

    @Value("${kakao.get-token.url}")
    private String getTokenUrl;

    @Value("${kakao.get-uerInfo.url}")
    private String getUserInfoUrl;

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public KaKaoLoginService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse kakaoLogin(String code) {
        RestClient client = RestClient.builder().build();
        ObjectMapper objectMapper = new ObjectMapper();
        String accessToken = getAccessToken(code);
        String email;

        ResponseEntity<String> response = client.get()
                .uri(URI.create(getUserInfoUrl))
                .header("Authorization", setAuthorizationHeader(accessToken))
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(KAKAO_LOGIN_FAILED_ERROR);
        }

        try {
            email = objectMapper.readValue(response.getBody(), KakaoUserInfo.class).kakao_account().email;
            return new AuthResponse(SaveMemberAndReturnJWT(email));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAccessToken(String code) {
        RestClient client = RestClient.builder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        LinkedMultiValueMap<String, String> body = createGetTokenBody(code);

        ResponseEntity<String> response = client.post()
                .uri(URI.create(getTokenUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(KAKAO_LOGIN_FAILED_ERROR);
        }

        try {
            return objectMapper.readValue(response.getBody(), KakaoTokenInfo.class).access_token();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private LinkedMultiValueMap<String, String> createGetTokenBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("redirect_url", redirectUrl);
        body.add("code", code);
        return body;
    }

    private String SaveMemberAndReturnJWT(String email) {
        Optional<Member> existMember = memberRepository.findMemberByEmail(email);
        if (existMember.isPresent()) {
            throw new CustomException(ALREADY_REGISTERED_ERROR);
        }

        Member savedMember = memberRepository.save(new Member(email, generateRandomPassword(), true));
        return jwtUtil.createJWT(savedMember.getId());
    }

    private String generateRandomPassword() {
        Random random = new Random();
        return String.valueOf(1000 + random.nextInt(9000));
    }

    private String setAuthorizationHeader(String token) {
        return "Bearer " + token;
    }

}
