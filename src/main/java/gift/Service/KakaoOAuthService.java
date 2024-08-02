package gift.Service;

import gift.Entity.KakaoUserEntity;
import gift.Mapper.KakaoUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import gift.DTO.KakaoUserDTO;

@Service
public class KakaoOAuthService {

    private final String CLIENT_ID = "abaef0417d13253471f9ad87ef210d09";
    private final String REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";

    @Autowired
    private KakaoUserService kakaoUserService;

    @Autowired
    private KakaoUserMapper kakaoUserMapper;

    public Map<String, String> getTokens(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, Map.class);

        Map<String, String> tokens = (Map<String, String>) response.getBody();
        return tokens;
    }

    // "엑세스토큰 -> 사용자 정보"를 다루는 메서드
    public KakaoUserDTO getUserInfo(String accessToken) {
        try {
            return requestUserInfo(accessToken);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // 액세스 토큰이 만료된 경우, 리프레시 토큰을 사용해 갱신
                return handleExpiredAccessToken(accessToken);
            } else {
                throw e;
            }
        }
    }
    
    // 실제로 엑세스토큰 -> 사용자 정보 를 가져오는 메서드
    private KakaoUserDTO requestUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, Map.class);

        Map<String, Object> userInfo = response.getBody();
        KakaoUserDTO userDTO = new KakaoUserDTO();
        userDTO.setKakaoId(userInfo.get("id").toString());
        userDTO.setEmail(((Map<String, String>) userInfo.get("kakao_account")).get("email"));

        return userDTO;
    }

    // 만료된 액세스 토큰을 처리하는 메서드
    private KakaoUserDTO handleExpiredAccessToken(String accessToken) {
        KakaoUserEntity userEntity = kakaoUserService.findByAccessToken(accessToken);
        if (userEntity != null) {
            String refreshToken = userEntity.getRefreshToken();
            Map<String, String> tokens = refreshAccessToken(refreshToken);
            String newAccessToken = tokens.get("access_token");

            // 업데이트된 액세스 토큰을 사용자 엔티티에 저장
            userEntity.setAccessToken(newAccessToken);
            kakaoUserService.saveOrUpdateUser(kakaoUserMapper.toDTO(userEntity), newAccessToken, refreshToken);

            // 새로운 액세스 토큰으로 다시 사용자 정보 요청
            return requestUserInfo(newAccessToken);
        }
        throw new RuntimeException("유저를 찾을 수가 없습니다.");
    }

    // 실제로 리프레시 토큰을 사용하여 갱신된 엑세스 토큰을 받아오는 메서드
    public Map<String, String> refreshAccessToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", CLIENT_ID);
        params.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, Map.class);

        Map<String, String> tokens = (Map<String, String>) response.getBody();
        return tokens;
    }
}
