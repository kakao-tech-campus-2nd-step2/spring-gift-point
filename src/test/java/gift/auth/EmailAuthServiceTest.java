package gift.auth;

import gift.dto.UserLoginDto;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.repository.UserRepository;
import gift.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class EmailAuthServiceTest {

    @Autowired
    private EmailAuthService emailAuthService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    private User user;

    @Test
    public void 이메일_로그인_성공() {
        user = new User("test@example.com", "password");
        UserLoginDto loginDto = new UserLoginDto("test@example.com", "password");

        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Mockito.when(tokenService.generateToken(anyString(), anyString())).thenReturn("mockToken");

        User loggedInUser = emailAuthService.loginUser(loginDto);

        assertNotNull(loggedInUser);
        assertEquals(user.getEmail(), loggedInUser.getEmail());
    }

    @Test
    public void 이메일_로그인_실패_잘못된_이메일() {
        user = new User("test@example.com", "password");
        UserLoginDto loginDto = new UserLoginDto("wrong@example.com", "password");

        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            emailAuthService.loginUser(loginDto);
        });

        assertEquals("이메일 또는 비밀번호가 잘못되었습니다.", exception.getMessage());
    }

    @Test
    public void 이메일_로그인_실패_잘못된_비밀번호() {
        user = new User("test@example.com", "password");
        UserLoginDto loginDto = new UserLoginDto("test@example.com", "wrongpassword");

        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            emailAuthService.loginUser(loginDto);
        });

        assertEquals("이메일 또는 비밀번호가 잘못되었습니다.", exception.getMessage());
    }

    @Test
    public void 토큰_생성_성공() {
        user = new User("test@example.com", "password");
        Mockito.when(tokenService.generateToken(anyString(), anyString())).thenReturn("mockToken");

        String token = emailAuthService.generateToken(user);

        assertEquals("mockToken", token);
    }
}
