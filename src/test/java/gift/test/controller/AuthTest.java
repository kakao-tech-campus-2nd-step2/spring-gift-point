package gift.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import gift.controller.AuthController;
import gift.entity.User;
import gift.service.AuthService;

public class AuthTest {
	
	@Mock
	private AuthService authService;
	
	@InjectMocks
	private AuthController authController;
	
	@Mock
	BindingResult bindingResult;
	
	private User user;
	
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("test@test.com", "pw");
    }
	
	@Test
    public void testRegister() {
        doNothing().when(authService).createUser(any(User.class), any(BindingResult.class));
        ResponseEntity<Void> response = authController.register(user, bindingResult);
        
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }
	
	@Test
    public void testLogin() {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", "dummyToken");
        when(authService.loginUser(any(User.class), any(BindingResult.class))).thenReturn(tokenMap);
        ResponseEntity<Map<String, String>> response = authController.login(user, bindingResult);

        assertThat(response.getBody().get("token")).isEqualTo("dummyToken");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
