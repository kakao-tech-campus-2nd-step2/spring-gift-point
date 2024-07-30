package gift.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import gift.controller.KakaoAuthController;
import gift.service.KakaoAuthService;

public class KakaoAuthTest {

	@Mock
	private KakaoAuthService kakaoAuthService;
	
	@InjectMocks
    private KakaoAuthController KakaoAuthController;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
    public void testKakaoRedirect() {
        String authorizationCode = "dummyAuthorizationCode";
        Map<String, String> accssTokenResponse = new HashMap<>();
        accssTokenResponse.put("access_token", "dummy_access_token");
        
        when(kakaoAuthService.getAccessToken(anyString())).thenReturn(accssTokenResponse);
        
        ResponseEntity<Map<String, String>> response = KakaoAuthController.kakaoRedirect(authorizationCode);
        
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().get("access_token")).isEqualTo("dummy_access_token");
    }
}
