package gift.service;

import gift.auth.JwtUtil;
import gift.domain.Role;
import gift.domain.User;
import gift.dto.requestdto.UserLoginRequestDTO;
import gift.dto.requestdto.UserSignupRequestDTO;
import gift.dto.responsedto.UserResponseDTO;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public UserResponseDTO register(UserSignupRequestDTO userSignupRequestDTO) {
        String token = jwtUtil.createToken(userSignupRequestDTO.email(),
            userSignupRequestDTO.role());
        return new UserResponseDTO(token);
    }

    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        User user = userLoginRequestDTO.toEntity();
        if (!user.getPassword().equals(userLoginRequestDTO.password())) {
            throw new NoSuchElementException("회원의 정보가 일치하지 않습니다.");
        }
        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        return new UserResponseDTO(token);
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

    @Deprecated
    public void authorizeAdminUser(User user, String productName) {
        boolean isContainKakao = productName.contains("카카오");
        if (!user.getRole().equals(Role.ADMIN.getRole()) && isContainKakao) {
            throw new IllegalStateException("권한이 없습니다.");
        }
    }
}
