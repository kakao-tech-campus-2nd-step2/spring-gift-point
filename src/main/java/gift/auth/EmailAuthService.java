package gift.auth;

import gift.dto.UserLoginDto;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.UserRepository;
import gift.service.TokenService;
import org.springframework.stereotype.Service;

@Service("emailAuthService")
public class EmailAuthService implements AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public EmailAuthService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public String getLoginUrl() {
        return "";
    }

    @Override
    public String generateToken(User user) {
        return tokenService.generateToken(user.getEmail(), "email");
    }

    public User loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (user.isPasswordIncorrect(userLoginDto.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        return user;
    }
}
