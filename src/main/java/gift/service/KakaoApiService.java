package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.Token;
import gift.component.kakao.KakaoApiProvider;
import gift.dto.KakaoUserInfoResponseDto;
import gift.auth.OAuthToken;
import gift.dto.KakaoMessageRequestDto;
import gift.vo.Member;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoApiService {

    private final MemberService memberService;
    private final KakaoApiProvider kakaoApiProvider;
    private final RestTemplate restTemplate;

    public KakaoApiService(MemberService memberService, KakaoApiProvider kakaoApiProvider, RestTemplate restTemplate) {
        this.memberService = memberService;
        this.kakaoApiProvider = kakaoApiProvider;
        this.restTemplate = restTemplate;
    }

    private Long getKakaoUserId(String accessToken) {
        HttpHeaders headers = kakaoApiProvider.makeHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoResponseDto> response = restTemplate.exchange(
                KakaoApiProvider.KAKAO_USER_PROFILE_URI,
                HttpMethod.POST,
                request,
                KakaoUserInfoResponseDto.class);

        return response.getBody().id();
    }

    public String getMemberEmailFromKakao(Token accessToken) {
        return KakaoApiProvider.KAKAO_EMAIL+getKakaoUserId(accessToken.token());
    }

    public Token getAccessToken(String code) {
        HttpHeaders headers = kakaoApiProvider.makeHeaders();
        MultiValueMap<String, String> body = kakaoApiProvider.makeGetAccessTokenBody(code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    KakaoApiProvider.KAKAO_TOKEN_REQUEST_URI,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            OAuthToken oauthToken = kakaoApiProvider.parseOAuthToken(response.getBody());

            return new Token(oauthToken.access_token());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex; // 예외를 던져 GlobalExceptionHandler에서 처리
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON response", e);
        }
    }

    /**
     * 회원 정보가 없다면 회원가입
     * @param accessToken Access Token
     */
    @Transactional
    public void kakaoLogin(Token accessToken) {
        String loginMemberEmail = getMemberEmailFromKakao(accessToken);

        boolean isNotExistMember = !memberService.hasMemberByEmail(loginMemberEmail);

        kakaoJoin(isNotExistMember, loginMemberEmail);
    }

    private void kakaoJoin(boolean noExistMember, String loginMemberEmail) {
        if (noExistMember) {
            memberService.join(new Member(loginMemberEmail, KakaoApiProvider.KAKAO_PASSWORD));
        }
    }

    public void sendKakaoMessage(Token token, KakaoMessageRequestDto kakaoMessageRequestDto) {
        HttpHeaders headers = kakaoApiProvider.makeHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token.token());

        MultiValueMap<String, String> body = kakaoApiProvider.makeTemplateObject(kakaoMessageRequestDto);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(KakaoApiProvider.KAKAO_MESSAGE_API_URI, HttpMethod.POST, request, String.class).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex;
        }
    }

}
