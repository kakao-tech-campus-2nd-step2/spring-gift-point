package gift.user.service;

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
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final JWTUtil jwtUtil;

  @Autowired
  public UserService(UserRepository userRepository, JWTUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
    this.jwtUtil = jwtUtil;
  }

  @Transactional
  public TokenResponseDto register(@Valid UserRequestDto userRequestDto) {
    String email = userRequestDto.email();
    String password = userRequestDto.password();

    if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
      throw new InvalidUserInputException("이메일이나 비밀번호가 비어있습니다.");
    }

    if (userRepository.existsByEmail(email)) {
      throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
    }

    User user = User.builder().email(email).password(passwordEncoder.encode(password))
        .role(UserRole.ROLE_USER).build();

    userRepository.save(user);

    String token = jwtUtil.createToken(user.getEmail(), user.getRole(), 24 * 60 * 60 * 1000L);
    return new TokenResponseDto(token, user.getEmail());
  }

  public TokenResponseDto authenticate(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new AuthenticationFailedException("이메일 또는 비밀번호가 올바르지 않습니다."));
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new AuthenticationFailedException("이메일 또는 비밀번호가 올바르지 않습니다.");
    }

    String token = jwtUtil.createToken(user.getEmail(), user.getRole(), 24 * 60 * 60 * 1000L);

    return new TokenResponseDto(token, user.getEmail());
  }

  public User getMemberFromToken(String token) {
    String email = jwtUtil.getEmail(token);
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("유효하지 않은 토큰입니다."));
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));
  }
}
