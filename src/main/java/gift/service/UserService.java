package gift.service;

import gift.common.auth.JwtTokenProvider;
import gift.common.exception.ErrorCode;
import gift.common.exception.UserException;
import gift.controller.user.dto.UserRequest;
import gift.controller.user.dto.UserResponse;
import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public Long register(UserRequest.Create userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new UserException(ErrorCode.EXIST_USER);
        }
        User user = userRepository.save(userRequest.toEntity());
        return user.getId();
    }

    public UserDto login(UserRequest.Login userRequest) {
        User user = userRepository.findByEmailAndPassword(userRequest.email(),
                userRequest.password())
            .orElseThrow(() -> new UserException(ErrorCode.INVALID_USER));

        String token = jwtTokenProvider.createToken(user.getEmail());

        return UserDto.from(token, user.getName());
    }
}
