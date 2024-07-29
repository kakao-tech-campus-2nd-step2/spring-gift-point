package gift.service;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomNotFoundException;
import gift.model.user.User;
import gift.model.user.UserDTO;
import gift.model.user.UserForm;
import gift.oauth.response.KakaoTokenResponse;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long insertUser(UserForm userForm) {
        return userRepository.save(new User(userForm.getEmail(), userForm.getPassword())).getId();
    }

    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.USER_NOT_FOUND));
        return new UserDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO findByKakaoId(Long kakaoId, KakaoTokenResponse token) {
        User user = userRepository.findByKakaoId(kakaoId)
            .orElseGet(() -> userRepository.save(
                new User(String.valueOf(kakaoId), String.valueOf(kakaoId), kakaoId,
                    token.accessToken(), token.refreshToken())));
        return new UserDTO(user);
    }

    @Transactional(readOnly = true)
    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean isPasswordMatch(UserForm userForm) {
        return userForm.getPassword()
            .equals(userRepository.findByEmail(userForm.getEmail())
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.USER_NOT_FOUND))
                .getPassword());
    }

    @Transactional(readOnly = true)
    public boolean isKakaoUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.USER_NOT_FOUND)).getKakaoId()
            != null;
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
