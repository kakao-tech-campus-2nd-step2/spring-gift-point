package gift.service;

import gift.dto.KakaoTokenResponseDTO;
import gift.dto.KakaoUserDTO;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class KakaoAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;

    @Autowired
    public KakaoAuthService(RestTemplate restTemplate, MemberRepository memberRepository) {
        this.restTemplate = restTemplate;
        this.memberRepository = memberRepository;
    }

    public KakaoTokenResponseDTO getKakaoToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponseDTO> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, KakaoTokenResponseDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("카카오 토큰을 찾을 수 없습니다.");
        }
    }

    // 카카오 사용자 정보 조회
    public KakaoUserDTO getKakaoUser(String accessToken) {
        String userUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<KakaoUserDTO> response = restTemplate.exchange(userUrl, HttpMethod.GET, request, KakaoUserDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
        }
    }

    // 사용자 등록 or 조회
    public Member registerOrGetMember(KakaoUserDTO kakaoUserDTO, String accessToken) {
        return memberRepository.findByEmail(kakaoUserDTO.getEmail())
            .orElseGet(() -> {
                // 카카오 로그인을 통해 가입한 회원의 경우 임의로 비밀번호 생성
                Member newMember = new Member(kakaoUserDTO.getEmail(), "kakao");
                newMember.setToken(accessToken);
                memberRepository.save(newMember);
                return newMember;
            });
    }

    public String getKakaoLoginUrl() {
        String url = "https://kauth.kakao.com/oauth/authorize";
        String queryString = String.format("?response_type=code&client_id=%s&redirect_uri=%s", clientId, redirectUri);
        return url + queryString;
    }

    public String getEmailFromAccessToken(String accessToken) {
        KakaoUserDTO kakaoUserDTO = getKakaoUser(accessToken);
        return kakaoUserDTO.getEmail();
    }
}
