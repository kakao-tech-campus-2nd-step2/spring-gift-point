package gift.ServiceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.KakaoLoginResponse;
import gift.domain.Member;
import gift.domain.WishList;
import gift.domain.getTokenDto;
import gift.repository.MemberRepository;
import gift.service.KakaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
