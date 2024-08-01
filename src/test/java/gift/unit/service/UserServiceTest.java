package gift.unit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

import gift.exception.CustomException;
import gift.user.dto.request.UserLoginRequest;
import gift.user.dto.request.UserRegisterRequest;
import gift.user.dto.response.UserResponse;
import gift.user.entity.User;
import gift.user.entity.UserRole;
import gift.user.repository.RoleJpaRepository;
import gift.user.repository.UserJpaRepository;
import gift.user.service.UserService;
import gift.util.auth.JwtUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private RoleJpaRepository roleRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("register user test")
    @Transactional
    void registerUserTest() {
        //given
        UserRegisterRequest request = new UserRegisterRequest("user1@email.com", "1q2w3e4r!", null,
            null);
        given(userRepository.findByEmail(request.email())).willReturn(Optional.empty());
        User user1 = new User(
            request.email(),
            request.password(),
            null,
            null
        );
        List<String> roles = new ArrayList<>();
        given(userRepository.save(any(User.class))).willReturn(user1);
        given(jwtUtil.generateToken(null, user1.getEmail(), roles)).willReturn("token");

        //when
        UserResponse actual = userService.registerUser(request);

        //then
        assertThat(actual.token()).isEqualTo("token");
        then(userRepository).should().findByEmail(request.email());
        then(userRepository).should().save(any(User.class));
        then(jwtUtil).should().generateToken(null, user1.getEmail(), roles);
    }

    @Test
    @DisplayName("Already Exist user registration test")
    @Transactional
    void alreadyExistUserRegistrationTest() {
        //given
        UserRegisterRequest request = new UserRegisterRequest("user1@example.com", "password1",
            null, null);
        given(userRepository.findByEmail(request.email())).willReturn(
            Optional.of(new User("user1@example.com", "password1", null, null)));

        //when&then
        assertThatThrownBy(() -> userService.registerUser(request))
            .isInstanceOf(CustomException.class);
        then(userRepository).should().findByEmail(request.email());
    }

    @Test
    @DisplayName("user login test")
    @Transactional
    void userLoginTest() {
        //given
        UserLoginRequest loginRequest = new UserLoginRequest("user1@example.com", "password1",
            null);
        Set<UserRole> roles = new HashSet<>();
        User user1 = new User(
            loginRequest.email(),
            loginRequest.password(),
            false,
            roles
        );
        List<String> rolesName = new ArrayList<>();
        given(userRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password()))
            .willReturn(Optional.of(user1));
        given(jwtUtil.generateToken(null, user1.getEmail(), rolesName)).willReturn("token");
        given(roleRepository.findAllById(any())).willReturn(List.of());

        //when
        UserResponse actual = userService.loginUser(loginRequest);

        //then
        assertThat(actual.token()).isEqualTo("token");
        then(userRepository).should().findByEmailAndPassword(loginRequest.email(),
            loginRequest.password());
    }

    @Test
    @DisplayName("unknown user login test")
    @Transactional
    void unknownUserLoginTest() {
        //given
        UserLoginRequest request = new UserLoginRequest("user1@email.com", "1q2w3e4r!", null);
        given(
            userRepository.findByEmailAndPassword(request.email(), request.password())).willReturn(
            Optional.empty());

        //when & then
        assertThatThrownBy(() -> userService.loginUser(request))
            .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("wrong password login test")
    @Transactional
    void wrongPasswordLoginTest() {
        //given
        UserLoginRequest request = new UserLoginRequest("user1@email.com", "1234", null);
        given(
            userRepository.findByEmailAndPassword(request.email(), request.password())).willReturn(
            Optional.empty());

        //when & then
        assertThatThrownBy(() -> userService.loginUser(request))
            .isInstanceOf(CustomException.class);
        then(userRepository).should(times(1)).findByEmailAndPassword(request.email(),
            request.password());
    }

    @Test
    @DisplayName("get user by id test")
    void getUserByIdTest() {
        // given
        User user1 = new User("user1@example.com", "password1", null, null);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        // when
        User actual = userService.getUserById(1L);

        // then
        assertThat(actual).isNotNull();
        then(userRepository).should().findById(1L);
    }

    @Test
    @DisplayName("get user by id not found test")
    void getUserByIdNotFoundTest() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserById(1L))
            .isInstanceOf(CustomException.class);
        then(userRepository).should().findById(1L);
    }

    @Test
    @DisplayName("get user id by token test")
    void getUserByTokenTest() {
        // given
        String token = "token";
        User user1 = new User("user1@example.com", "password1", null, null);
        given(jwtUtil.extractUserId(token)).willReturn(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        // when
        User user = userService.getUserByToken(token);

        // then
        then(jwtUtil).should().extractUserId(token);
        then(userRepository).should().findById(1L);
    }

    @Test
    @DisplayName("invalid token test")
    void invalidTokenTest() {
        // given
        String token = "invalid_token";
        given(jwtUtil.extractUserId(token)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> userService.getUserByToken(token))
            .isInstanceOf(CustomException.class);
        then(jwtUtil).should().extractUserId(token);
    }
}
