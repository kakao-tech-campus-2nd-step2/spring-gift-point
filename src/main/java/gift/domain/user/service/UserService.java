package gift.domain.user.service;

import gift.auth.jwt.JwtProvider;
import gift.auth.jwt.JwtToken;
import gift.domain.user.dto.UserLoginRequest;
import gift.domain.user.dto.UserRequest;
import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import gift.domain.user.repository.UserJpaRepository;
import gift.exception.InvalidUserInfoException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final JwtProvider jwtProvider;

    public UserService(UserJpaRepository userJpaRepository, JwtProvider jwtProvider) {
        this.userJpaRepository = userJpaRepository;
        this.jwtProvider = jwtProvider;
    }

    public JwtToken signUp(UserRequest userRequest) {
        User user = userRequest.toUser();
        User savedUser = userJpaRepository.save(user);
        
        return jwtProvider.generateToken(savedUser);
    }

    public JwtToken login(UserLoginRequest userLoginRequest) {
        User user = userJpaRepository.findByEmail(userLoginRequest.email())
            .orElseThrow(() -> new InvalidUserInfoException("error.invalid.userinfo.email"));

        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new InvalidUserInfoException("error.invalid.userinfo.provider");
        }

        if (!user.checkPassword(userLoginRequest.password())) {
            throw new InvalidUserInfoException("error.invalid.userinfo.password");
        }

        return jwtProvider.generateToken(user);
    }

    public Role verifyRole(JwtToken jwtToken) {
        return jwtProvider.getAuthorization(jwtToken.token());
    }
}
