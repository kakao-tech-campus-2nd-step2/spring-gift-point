package gift.user.application;


import gift.user.domain.LoginType;
import gift.user.domain.User;
import gift.user.domain.UserRegisterRequest;
import gift.user.exception.UserException;
import gift.user.infra.UserRepository;
import gift.util.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public User registerUser(UserRegisterRequest request) {
        if (request.getLoginType() == LoginType.NORMAL) {
            // 일반 사용자 등록
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            // 중복회원 검증
            validateDuplicateUser(request.getEmail());

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(encodedPassword);
            user.setName(request.getName());
            user.setLoginType(LoginType.NORMAL);
            return userRepository.save(user);
        } else if (request.getLoginType() == LoginType.KAKAO) {
            // 카카오 사용자 등록
            validateDuplicateKakaoUser(request.getKakaoId());
            User user = new User(request.getName(), request.getProfileImageUrl(), request.getKakaoId());
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
        System.out.println("user.getPassword() = " + user.getPassword());
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }


}


