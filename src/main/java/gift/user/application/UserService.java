package gift.user.application;

import gift.user.domain.*;
import gift.user.exception.UserException;
import gift.user.infra.UserRepository;
import gift.util.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new UserException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User registerUser(UserRegisterRequest request) {
        if (request.getLoginType() == LoginType.NORMAL) {
            if (!(request instanceof NormalUserRegisterRequest)) {
                throw new UserException(ErrorCode.INVALID_USER_TYPE);
            }
            NormalUserRegisterRequest normalRequest = (NormalUserRegisterRequest) request;
            String encodedPassword = passwordEncoder.encode(normalRequest.getPassword());
            validateDuplicateUser(normalRequest.getEmail());
            User user = new User(normalRequest.getName(), normalRequest.getEmail());
            user.setPassword(encodedPassword);
            return userRepository.save(user);
        } else if (request.getLoginType() == LoginType.KAKAO) {
            if (!(request instanceof KakaoUserRegisterRequest)) {
                throw new UserException(ErrorCode.INVALID_USER_TYPE);
            }
            KakaoUserRegisterRequest kakaoRequest = (KakaoUserRegisterRequest) request;
            validateDuplicateKakaoUser(kakaoRequest.getKakaoId());
            User user = new User(kakaoRequest.getName(), kakaoRequest.getProfileImageUrl(), kakaoRequest.getKakaoId());
            return userRepository.save(user);
        } else {
            throw new UserException(ErrorCode.INVALID_USER_TYPE);
        }
    }

    private void validateDuplicateUser(String email) {
        userRepository.findByEmail(email).ifPresent(
                user -> {
                    throw new UserException(ErrorCode.DUPLICATE_USER);
                }
        );
    }

    private void validateDuplicateKakaoUser(Long kakaoId) {
        userRepository.findByKakaoId(kakaoId).ifPresent(
                user -> {
                    throw new UserException(ErrorCode.DUPLICATE_USER);
                }
        );
    }

    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
