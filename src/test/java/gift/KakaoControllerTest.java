package gift;

import gift.entity.KakaoToken;
import gift.repository.KakaoTokenRepository;
import gift.repository.MemberRepository;
import gift.service.KakaoService;
import gift.exception.MemberNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import gift.entity.Member;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KakaoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private KakaoTokenRepository kakaoTokenRepository;

    @Autowired
    private MemberRepository memberRepository;


    @MockBean
    private KakaoService kakaoService;

    // "/kakao/login" 엔드포인트에 GET 요청
    @Test
    public void testLogin() {
        String url = "http://localhost:" + port + "/kakao/login";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    // 유효한 코드로 카카오 로그인을 시도하고, 성공 시 DB에 토큰 정보가 저장되는지 확인하는 테스트
    @Test
    @Transactional
    public void testCallbackKakaoSuccess() {
        String code = "valid_code";
        String accessToken = "valid_access_token";
        String email = "user@example.com";
        String refreshToken = "valid_refresh_token";

        // 카카오 서비스 로그인 모킹
        when(kakaoService.login(code)).thenReturn(accessToken);

        // 카카오 토큰을 저장하는 로직을 추가합니다.
        KakaoToken kakaoToken = new KakaoToken(email, accessToken, refreshToken);
        kakaoTokenRepository.save(kakaoToken);

        String url = "http://localhost:" + port + "/kakao/oauth2/callback?code=" + code;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


        // DB에 토큰 정보가 저장되었는지 확인
        KakaoToken storedToken = kakaoTokenRepository.findByAccessToken(accessToken);
        assertThat(storedToken).isNotNull();
        assertThat(storedToken.getAccessToken()).isEqualTo(accessToken);
        assertThat(storedToken.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(storedToken.getEmail()).isEqualTo(email);
    }



    // 회원을 찾을 수 없는 경우
    @Test
    public void testCallbackKakaoMemberNotFound() {
        String code = "valid_code";


        when(kakaoService.login(code)).thenThrow(MemberNotFoundException.class);


        String url = "http://localhost:" + port + "/kakao/oauth2/callback?code=" + code;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("register");
    }

