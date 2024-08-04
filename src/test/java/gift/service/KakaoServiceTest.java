package gift.service;

import gift.entity.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class KakaoServiceTest {
    @Mock
    private KakaoAuthProvider kakaoAuthProvider;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KakaoService kakaoService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        when(kakaoAuthProvider.getRestTemplate()).thenReturn(restTemplate);
    }

    @Test
    void getAccessTokenSuccess() {
        String authorizationCode = "test_code";
        String accessToken = "test_access_token";
        String tokenRequestUri = "https://token.request.uri";

        when(kakaoAuthProvider.getTokenRequestUri()).thenReturn("https://token.request.uri");
        when(kakaoAuthProvider.getClientId()).thenReturn("test_client_id");
        when(kakaoAuthProvider.getRedirectUri()).thenReturn("https://redirect.uri");

        mockServer.expect(ExpectedCount.once(), requestTo(tokenRequestUri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"access_token\": \"" + accessToken + "\"}", MediaType.APPLICATION_JSON));

        String result = kakaoService.getAccessToken(authorizationCode);

        assertEquals(accessToken, result);
        mockServer.verify();
    }

    @Test
    void getUserSuccess() {
        String accessToken = "test_access_token";
        String memberInfoRequestUri = "https://member.request.uri";

        when(kakaoAuthProvider.getMemberInfoRequestUri()).thenReturn(memberInfoRequestUri);

        mockServer.expect(ExpectedCount.once(), requestTo(memberInfoRequestUri))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer " + accessToken))
                .andRespond(withSuccess("{\"id\": \"test_id\", \"properties\": {\"nickname\": \"test_nickname\"}, \"kakao_account\": {\"email\": \"test@example.com\"}, \"kakao_id\": \"test_id\"}", MediaType.APPLICATION_JSON));

        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Member result = kakaoService.getMember(accessToken);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
<<<<<<< HEAD
        assertEquals("test_nickname", result.getName());
=======
        assertEquals("test_nickname", result.getNickname());
>>>>>>> e3b9ef38d18104514aa1d0951ff1a098ff9a093f
        mockServer.verify();
    }
}
