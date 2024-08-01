package gift.service;

import gift.domain.KakaoTokenResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KakaoLoginServiceTest {

    @Mock
    private RestClient restClient;

    @InjectMocks
    private KakaoLoginService kakaoLoginService;

    @BeforeEach
    void setUp() {
        kakaoLoginService = new KakaoLoginService("testClientId", "testRedirectUri", "testClientSecret");
    }

    @Test
    void testGetToken() {
        // Given
        String code = "testCode";
        KakaoTokenResponseDTO expectedResponse = new KakaoTokenResponseDTO();
        expectedResponse.setAccessToken("testAccessToken");

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(KakaoTokenResponseDTO.class)).thenReturn(expectedResponse);

        // When
        String result = kakaoLoginService.getToken(code);

        // Then
        assertEquals("testAccessToken", result);
        verify(restClient).post();
        verify(requestBodyUriSpec).uri(URI.create("https://kauth.kakao.com/oauth/token"));
        verify(requestBodyUriSpec).contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Test
    void testGetEmail() {
        // Given
        String token = "testToken";
        KakaoTokenResponseDTO expectedResponse = new KakaoTokenResponseDTO();
        KakaoTokenResponseDTO.KakaoAccountInfo kakaoAccountInfo = new KakaoTokenResponseDTO.KakaoAccountInfo();
        kakaoAccountInfo.setEmail("test@example.com");
        expectedResponse.setKakaoAccount(kakaoAccountInfo);

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(KakaoTokenResponseDTO.class)).thenReturn(expectedResponse);

        // When
        String result = kakaoLoginService.getEmail(token);

        // Then
        assertEquals("test@example.com", result);
        verify(restClient).post();
        verify(requestBodyUriSpec).uri(URI.create("https://kapi.kakao.com/v2/user/me"));
        verify(requestBodyUriSpec).header("Authorization", "Bearer " + token);
    }

    @Test
    void testSendMessage() {
        // Given
        String token = "testToken";
        String message = "testMessage";
        KakaoTokenResponseDTO expectedResponse = new KakaoTokenResponseDTO();

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(KakaoTokenResponseDTO.class)).thenReturn(expectedResponse);

        // When
        kakaoLoginService.sendMessage(token, message);

        // Then
        verify(restClient).post();
        verify(requestBodyUriSpec).uri(URI.create("https://kapi.kakao.com/v2/api/talk/memo/default/send"));
        verify(requestBodyUriSpec).header("Authorization", "Bearer " + token);
    }

    @Test
    void testRequestWithClientError() {
        // Given
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            RestClient.ResponseSpec.ErrorHandler handler = invocation.getArgument(1);
            return responseSpec;
        });

        // When & Then
        assertThrows(RuntimeException.class, () ->
                kakaoLoginService.request("https://example.com", body, null)
        );
    }

    @Test
    void testRequestWithServerError() {
        // Given
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            RestClient.ResponseSpec.ErrorHandler handler = invocation.getArgument(1);
            return responseSpec;
        });

        // When & Then
        assertThrows(RuntimeException.class, () ->
                kakaoLoginService.request("https://example.com", body, null)
        );
    }
}