package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import gift.entity.User;
import gift.repository.UserRepository;
import gift.service.AuthService;
import gift.service.TokenService;
import gift.service.UserService;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;
    
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @Mock
    private BindingResult bindingResult;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("test@test.com", "pw");
        user.setId(1L);
    }

    @Test
    public void testLoginUser() {
        when(userService.findByEmail(any(String.class))).thenReturn(user);
        Map<String, String> tokenMap = authService.loginUser(user, bindingResult);

        verify(userService).findByEmail(any(String.class));
        assertThat(tokenMap).containsKey("access_token");
    }

    @Test
    public void testParseToken() {
        String token = authService.grantAccessToken(user);
        String email = authService.parseToken("Bearer " + token);

        assertThat(email).isEqualTo(user.getEmail());
    }

    @Test
    public void testGrantAccessToken() {
        String token = authService.grantAccessToken(user);
        assertThat(token).isNotNull();
    }
}
