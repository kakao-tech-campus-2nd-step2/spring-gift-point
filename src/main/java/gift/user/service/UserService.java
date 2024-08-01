package gift.user.service;

import gift.exception.BadRequestException;
import gift.exception.InvalidUserInputException;
import gift.exception.ResourceNotFoundException;
import gift.user.dto.UserDto;
import gift.user.entity.User;
import gift.user.entity.UserRole;
import gift.user.repository.UserRepository;
import gift.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;

  @Autowired
  public UserService(UserRepository userRepository, JwtTokenProvider tokenProvider) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
    this.tokenProvider = tokenProvider;
  }

  public String register(@Valid UserDto userDto) {
    if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
      throw new BadRequestException("이미 존재하는 이메일입니다.");
    }

    if (userDto.getEmail() == null || userDto.getPassword() == null) {
      throw new InvalidUserInputException("이메일이나 비밀번호가 비어있습니다.");
    }


    User user = new User();
    user.setEmail(userDto.getEmail());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setRole(userDto.getUserRole());

    userRepository.save(user);
    return tokenProvider.createToken(user.getEmail());
  }

  public String authenticate(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new BadRequestException("이메일 또는 비밀번호가 올바르지 않습니다."));
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadRequestException("이메일 또는 비밀번호가 올바르지 않습니다.");
    }
    return tokenProvider.createToken(user.getEmail());
  }

  public User getMemberFromToken(String token) {
    String email = tokenProvider.getEmailFromToken(token);
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("유효하지 않은 토큰입니다."));
  }

}
