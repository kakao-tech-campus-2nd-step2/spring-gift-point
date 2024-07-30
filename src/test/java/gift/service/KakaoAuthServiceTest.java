package gift.service;

import gift.config.KakaoProperties;
import gift.model.KakaoAuth;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
class KakaoAuthServiceTest {
    @Mock
    private KakaoProperties kakaoProperties;

    @Mock
    private ProductService productService;

    @Mock
    private OptionService optionService;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.Builder restClientBuilder;


    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;


    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private KakaoAuthService kakaoAuthService;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();

        mockServer = MockRestServiceServer.createServer(restTemplate);

        when(restClientBuilder.requestFactory(any())).thenReturn(restClientBuilder);
        when(restClientBuilder.requestInterceptor(any())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);

        when(restClient.post()).thenReturn(requestBodyUriSpec);

        when(requestBodyUriSpec.uri(any(URI.class))).thenAnswer(invocation -> requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenAnswer(invocation -> requestBodySpec);
        when(requestBodySpec.body(any(LinkedMultiValueMap.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        when(kakaoProperties.getClientId()).thenReturn("mock-client-id");
        when(kakaoProperties.getRedirectUrl()).thenReturn("mock-redirect-url");

        KakaoAuth mockKakaoAuth = new KakaoAuth();
        when(responseSpec.toEntity(KakaoAuth.class)).thenReturn(ResponseEntity.ok(mockKakaoAuth));

        kakaoAuthService = new KakaoAuthService(restClientBuilder, kakaoProperties, productService,
                optionService, optionRepository, memberRepository, wishRepository);

    }

    @Test
    @DisplayName("getKakaoTokenTest")
    void getKakaoTokenTest() {
        String authorizationCode = "authorization code";
        String accessToken = "access token";
        String response = String.format("{\"access_token\":\"%s\"}", accessToken);

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=utf-8"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        kakaoAuthService.getKakaoToken(authorizationCode);

        verify(requestBodySpec).body(any(LinkedMultiValueMap.class));
        verify(responseSpec).toEntity(KakaoAuth.class);
    }
}