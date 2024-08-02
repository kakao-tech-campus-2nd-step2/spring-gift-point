package gift.service;

import gift.exception.AuthenticationFailedException;
import gift.exception.DuplicateEmailException;
import gift.exception.InvalidUserInputException;
import gift.exception.ResourceNotFoundException;
import gift.security.JWTUtil;
import gift.user.dto.TokenResponseDto;
import gift.user.dto.UserRequestDto;
import gift.user.entity.User;
import gift.user.entity.UserRole;
import gift.user.repository.UserRepository;
import gift.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private JWTUtil jwtUtil;

  @InjectMocks
  private UserService userService;

  private BCryptPasswordEncoder passwordEncoder;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    passwordEncoder = new BCryptPasswordEncoder();
  }

  @Test
  public void testRegisterSuccess() {
    // Given
    UserRequestDto userRequestDto = new UserRequestDto("test@example.com", "password123");
    when(userRepository.existsByEmail(userRequestDto.email())).thenReturn(false);
    when(userRepository.save(ArgumentMatchers.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    String token = "dummyToken";
    when(jwtUtil.createToken(userRequestDto.email(), UserRole.ROLE_USER, 24 * 60 * 60 * 1000L)).thenReturn(token);

    // When
    TokenResponseDto response = userService.register(userRequestDto);

    // Then
    assertEquals(token, response.token());
    assertEquals(userRequestDto.email(), response.email());
    verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
  }

  @Test
  public void testRegisterEmailAlreadyExists() {
    // Given
    UserRequestDto userRequestDto = new UserRequestDto("test@example.com", "password123");
    when(userRepository.existsByEmail(userRequestDto.email())).thenReturn(true);

    // When & Then
    DuplicateEmailException thrown = assertThrows(DuplicateEmailException.class, () -> {
      userService.register(userRequestDto);
    });

    assertEquals("이미 존재하는 이메일입니다.", thrown.getMessage());
  }


  @Test
  public void testAuthenticateSuccess() {
    // Given
    String email = "test@example.com";
    String password = "password123";
    User user = User.builder().email(email).password(passwordEncoder.encode(password)).role(UserRole.ROLE_USER).build();
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    String token = "dummyToken";
    when(jwtUtil.createToken(email, UserRole.ROLE_USER, 24 * 60 * 60 * 1000L)).thenReturn(token);

    // When
    TokenResponseDto response = userService.authenticate(email, password);

    // Then
    assertEquals(token, response.token());
    assertEquals(email, response.email());
  }

  @Test
  public void testAuthenticateInvalidPassword() {
    // Given
    String email = "test@example.com";
    String password = "wrongPassword";
    User user = User.builder().email(email).password(passwordEncoder.encode("correctPassword")).role(UserRole.ROLE_USER).build();
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // When & Then
    AuthenticationFailedException thrown = assertThrows(AuthenticationFailedException.class, () -> {
      userService.authenticate(email, password);
    });

    assertEquals("이메일 또는 비밀번호가 올바르지 않습니다.", thrown.getMessage());
  }

  @Test
  public void testGetMemberFromTokenSuccess() {
    // Given
    String token = "validToken";
    String email = "test@example.com";
    User user = User.builder().email(email).role(UserRole.ROLE_USER).build();
    when(jwtUtil.getEmail(token)).thenReturn(email);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // When
    User result = userService.getMemberFromToken(token);

    // Then
    assertEquals(email, result.getEmail());
  }

  @Test
  public void testGetMemberFromTokenInvalidToken() {
    // Given
    String token = "invalidToken";
    when(jwtUtil.getEmail(token)).thenReturn("nonexistent@example.com");
    when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

    // When & Then
    ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
      userService.getMemberFromToken(token);
    });

    assertEquals("유효하지 않은 토큰입니다.", thrown.getMessage());
  }

  @Test
  public void testGetUserByEmailSuccess() {
    // Given
    String email = "test@example.com";
    User user = User.builder().email(email).role(UserRole.ROLE_USER).build();
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // When
    User result = userService.getUserByEmail(email);

    // Then
    assertEquals(email, result.getEmail());
  }

  @Test
  public void testGetUserByEmailNotFound() {
    // Given
    String email = "nonexistent@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // When & Then
    ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
      userService.getUserByEmail(email);
    });

    assertEquals("사용자를 찾을 수 없습니다", thrown.getMessage());
  }
}
