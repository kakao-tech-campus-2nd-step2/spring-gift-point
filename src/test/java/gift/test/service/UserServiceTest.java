package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import gift.entity.User;
import gift.repository.UserRepository;
import gift.service.TokenService;
import gift.service.UserService;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User("test@test.com", "pw");
        user.setId(1L);
    }

    @Test
    public void testGetUserFromToken() {
        when(tokenService.extractionEmail(anyString())).thenReturn("test@test.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserFromToken("token");

        verify(tokenService).extractionEmail(anyString());
        verify(userRepository).findByEmail(anyString());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }
}
