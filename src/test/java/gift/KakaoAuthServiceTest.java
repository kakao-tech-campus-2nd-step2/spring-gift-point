package gift;

import gift.service.KakaoAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class KakaoAuthServiceTest {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KakaoAuthService kakaoAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testKakaoLoginRedirect() {
        var url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message,account_email&response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;

        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.FOUND);
        when(restTemplate.getForEntity(URI.create(url), String.class)).thenReturn(mockResponse);

        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(url), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }
}
