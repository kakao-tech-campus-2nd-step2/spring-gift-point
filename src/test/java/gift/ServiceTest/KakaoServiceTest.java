package gift.ServiceTest;

import gift.repository.MemberRepository;
import gift.service.KakaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class KakaoServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private KakaoService kakaoService;

    @Test
    @DisplayName("Uri 생성 테스트")
    void testMakeUri() {
        URI uri = kakaoService.makeUri();
        assertNotNull(uri);
        assertTrue(uri.toString().contains("https://kauth.kakao.com/oauth/authorize"));
        assertTrue(uri.toString().contains("client_id="));
    }

}
