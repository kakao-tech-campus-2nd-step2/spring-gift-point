package gift.service;

import gift.auth.JwtUtil;
import gift.auth.KakaoClient;
import gift.auth.dto.KakaoProperties;
import gift.auth.dto.KakaoUserInfo;
import gift.domain.Role;
import gift.domain.User;
import gift.dto.requestdto.UserLoginRequestDTO;
import gift.dto.requestdto.UserSignupRequestDTO;
import gift.dto.responsedto.UserResponseDTO;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {
    private final JwtUtil jwtUtil;
    private final KakaoClient kakaoClient;

    public AuthService(JwtUtil jwtUtil, KakaoClient kakaoClient) {
        this.jwtUtil = jwtUtil;
        this.kakaoClient = kakaoClient;
    }

    public UserResponseDTO register(UserSignupRequestDTO userSignupRequestDTO) {
        String token = jwtUtil.createToken(userSignupRequestDTO.email(),
            userSignupRequestDTO.role());
        return new UserResponseDTO(token);
    }

    public UserResponseDTO login(User user, UserLoginRequestDTO userLoginRequestDTO) {
        if (!user.getPassword().equals(userLoginRequestDTO.password())) {
            throw new NoSuchElementException("회원의 정보가 일치하지 않습니다.");
        }
        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        user.updateAccessToken(token);
        return new UserResponseDTO(token);
    }

    public String getUserEmail(String accessToken){
        return kakaoClient.getUserEmail(accessToken).kakaoAccount().email();
    }

    public void authorizeUser(User user, Long userId) {
        if (!user.getId().equals(userId)) {
            throw new IllegalStateException("권한이 없습니다.");
        }
    }

    public void authorizeAdminUser(User user) {
        if (!user.getRole().equals(Role.ADMIN.getRole())) {
            throw new IllegalStateException("권한이 없습니다.");
        }
    }

    public String getAccessToken(String code){
        return kakaoClient.getAccessToken(code);
    }

    public KakaoProperties getProperties(){
        return kakaoClient.getProperties();
    }

    @Deprecated
    public void authorizeAdminUser(User user, String productName) {
        boolean isContainKakao = productName.contains("카카오");
        if (!user.getRole().equals(Role.ADMIN.getRole()) && isContainKakao) {
            throw new IllegalStateException("권한이 없습니다.");
        }
    }
}
