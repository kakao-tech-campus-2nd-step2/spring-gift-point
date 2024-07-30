package gift.service;

import gift.auth.AuthService;
import gift.auth.EmailAuthService;
import gift.dto.UserLoginDto;
import gift.dto.UserRegisterDto;
import gift.dto.UserResponseDto;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.mapper.UserMapper;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthService emailAuthService;
    private final AuthService kakaoAuthService;

    public UserService(UserRepository userRepository, AuthService emailAuthService, AuthService kakaoAuthService) {
        this.userRepository = userRepository;
        this.emailAuthService = emailAuthService;
        this.kakaoAuthService = kakaoAuthService;
    }

    public UserResponseDto registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = new User(userRegisterDto.getEmail(), userRegisterDto.getPassword());
        User createdUser = userRepository.save(user);

        return UserMapper.toUserResponseDTO(createdUser);
    }

    public String loginUser(UserLoginDto userLoginDto) {
        User user = ((EmailAuthService) emailAuthService).loginUser(userLoginDto);
        return emailAuthService.generateToken(user);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        User user = getUserEntityById(id);
        return UserMapper.toUserResponseDTO(user);
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponseDto updateUser(Long id, UserRegisterDto userRegisterDto) {
        User existingUser = getUserEntityById(id);
        existingUser.update(userRegisterDto.getEmail(), userRegisterDto.getPassword());
        userRepository.save(existingUser);
        return UserMapper.toUserResponseDTO(existingUser);
    }

    public void deleteUser(Long id) {
        User user = getUserEntityById(id);
        userRepository.delete(user);
    }
}
