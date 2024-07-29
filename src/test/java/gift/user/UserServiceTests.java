package gift.user;

import gift.core.domain.user.UserAccount;
import gift.core.domain.user.UserAccountRepository;
import gift.core.domain.user.*;
import gift.core.domain.user.exception.UserAlreadyExistsException;
import gift.core.domain.user.exception.UserNotFoundException;
import gift.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, userAccountRepository);
    }

    @Test
    public void testRegisterUser() {
        User user = new User(0L, "test", new UserAccount("principal", "credentials"));

        when(userRepository.existsById(0L)).thenReturn(false);
        when(userAccountRepository.existsByPrincipal("principal")).thenReturn(false);

        userService.registerUser(user);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("이미 존재하는 사용자 ID로 사용자 등록 시도 테스트")
    public void testRegisterUserWithExistingUserId() {
        User user = new User(0L, "test", new UserAccount("principal", "credentials"));

        when(userRepository.existsById(0L)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
    }

    @Test
    @DisplayName("이미 존재하는 사용자 계정으로 사용자 등록 시도 테스트")
    public void testRegisterUserWithExistingUserAccount() {
        User user = new User(0L, "test", new UserAccount("principal", "credentials"));

        when(userRepository.existsById(0L)).thenReturn(false);
        when(userAccountRepository.existsByPrincipal("principal")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User(userId, "test", new UserAccount("principal", "credentials"));
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.getUserById(userId);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 사용자 조회 시도 테스트")
    public void testGetUserByIdWithNonExistingUserId() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    public void testDeleteUserById() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUserById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 사용자 삭제 시도 테스트")
    public void testDeleteUserByIdWithNonExistingUserId() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
    }
}
