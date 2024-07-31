package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class KakaoMessageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KakaoMessageService kakaoMessageService;

    private String messageUrl = "http://fakeurl.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // private 필드를 설정하기 위해 reflection 사용
        ReflectionTestUtils.setField(kakaoMessageService, "messageUrl", messageUrl);
    }

    @Test
    @DisplayName("유효한 주문으로 카카오 메시지 전송 테스트")
    void testSendMessage_Success() throws JsonProcessingException {
        // Given
        OrderResponse orderResponse = new OrderResponse(1L, 1L, 1, LocalDateTime.now(), "Test message");
        String accessToken = "validToken";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(eq(messageUrl), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        // When
        kakaoMessageService.sendMessage(orderResponse, accessToken);

        // Then
        verify(restTemplate, times(1)).postForEntity(eq(messageUrl), any(HttpEntity.class), eq(String.class));
    }

    @Test
    @DisplayName("잘못된 토큰으로 카카오 메시지 전송 실패 테스트")
    void testSendMessage_Failure() throws JsonProcessingException {
        // Given
        OrderResponse orderResponse = new OrderResponse(1L, 1L, 1, LocalDateTime.now(), "Test message");
        String accessToken = "invalidToken";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        when(restTemplate.postForEntity(eq(messageUrl), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        // When & Then
        assertThrows(RuntimeException.class, () -> kakaoMessageService.sendMessage(orderResponse, accessToken));
        verify(restTemplate, times(1)).postForEntity(eq(messageUrl), any(HttpEntity.class), eq(String.class));
    }
}