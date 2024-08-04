package gift.controller.rest;

import gift.model.response.MessageResponseDTO;
import gift.model.user.*;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "User 컨트롤러", description = "User API입니다.")
@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원가입", description = "회원가입입니다.")
    @PostMapping("/register")
    public ResponseEntity<AccessTokenResponse> signup(@RequestBody @Valid UserDTO userDTO, HttpSession session) {
        String accessToken = userService.signup(userDTO);
        session.setAttribute("email", userDTO.getEmail());
        return ResponseEntity.ok().body(makeAccessTokenResponse(accessToken));
    }

    @Operation(summary = "로그인", description = "로그인입니다.")
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody @Valid UserDTO form, HttpSession session) {
        String accessToken = userService.login(form);
        session.setAttribute("email", form.getEmail());
        return ResponseEntity.ok().body(makeAccessTokenResponse(accessToken));
    }

    @Operation(summary = "로그아웃", description = "로그아웃입니다.")
    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDTO> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body(new MessageResponseDTO("Logged out successfully"));
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인입니다.")
    @PostMapping("/kakao")
    public ResponseEntity<AccessTokenResponse> kakaoLogin(@RequestBody Map<String, String> request, HttpSession session) {
        String code = request.get("code");

        String kakaoAccessToken = userService.kakaoLogin(code);
        Map<String, String> response = userService.getKakaoProfile(kakaoAccessToken);

        session.setAttribute("email", response.get("email"));
        session.setAttribute("kakaoAccessToken", kakaoAccessToken);

        return ResponseEntity.ok().body(makeAccessTokenResponse(response.get("accessToken")));
    }

    @Operation(summary = "마이페이지", description = "마이페이지입니다.")
    @GetMapping("/me")
    public ResponseEntity<UserPointChargeDTO> me(HttpSession session) {
        String email = (String) session.getAttribute("email");
        User user = userService.me(email);

        UserPointChargeDTO res = new UserPointChargeDTO(user);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "포인트 충전", description = "포인트 충전 페이지입니다.")
    @PostMapping("/charge")
    public ResponseEntity<MessageResponseDTO> chargePoint(@RequestBody UserPointChargeRequest form) {
        userService.chargePoint(form);
        return ResponseEntity.ok().body(new MessageResponseDTO("point charged successfully"));
    }

    private AccessTokenResponse makeAccessTokenResponse(String accessToken) {
        return new AccessTokenResponse(accessToken);
    }
}
