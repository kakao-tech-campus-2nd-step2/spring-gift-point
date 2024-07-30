package gift.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KakaoLoginServiceTest {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${kakao.login.REST-API-KEY}")
    private String loginRestApiKey;

    @Value("${kakao.login.REDIRECT-URI}")
    private String loginRedirectUri;

    private static String testUri;

    @BeforeAll
    static void setUp(@Autowired KakaoLoginService kakaoLoginService,
                      @Value("${kakao.login.REST-API-KEY}") String loginRestApiKey,
                      @Value("${kakao.login.REDIRECT-URI}") String loginRedirectUri) {
        testUri = kakaoLoginService.makeKakaoAuthorizationURI();
    }

    @Test
    void testMakeKakaoAuthorizationURI() {
        String expectedUri = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + loginRestApiKey + "&redirect_uri=" + loginRedirectUri;
        assertEquals(expectedUri, testUri, "성공");
    }
    /*
    @Test
    void testGetKakaoAuthorizationToken(){
        String uri = kakaoLoginService.makeKakaoAuthorizationURI();
        String code = extractCodeFromUri(uri);
        System.out.println("code  : " +  code);
    }

    public String extractCodeFromUri(String uri){
        return uri.substring(28);
    }
    */
}

