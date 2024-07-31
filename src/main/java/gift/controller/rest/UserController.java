package gift.controller.rest;

import gift.entity.AccessTokenResponseDTO;
import gift.entity.MessageResponseDTO;
import gift.entity.UserDTO;
import gift.entity.UserResponseDTO;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "User 컨트롤러", description = "User API입니다.")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원가입", description = "회원가입입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = AccessTokenResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<AccessTokenResponseDTO> signup(@RequestBody @Valid UserDTO userDTO, HttpSession session) {
        String accessToken = userService.signup(userDTO);
        session.setAttribute("email", userDTO.getEmail());
        session.setAttribute("role", "USER");
        return ResponseEntity.ok().body(makeAccessTokenResponse(accessToken));
    }

    @Operation(summary = "로그인", description = "로그인입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = AccessTokenResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "존재하지 않는 이메일 / 올바르지 않은 비밀번호",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponseDTO> login(@RequestBody @Valid UserDTO form, HttpSession session) {
        String accessToken = userService.login(form);
        session.setAttribute("email", form.getEmail());
        session.setAttribute("role", userService.findOne(form.getEmail()).getRole());
        return ResponseEntity.ok().body(makeAccessTokenResponse(accessToken));
    }

    @Operation(summary = "로그아웃", description = "로그아웃입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDTO> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body(new MessageResponseDTO("Logged out successfully"));
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카카오 로그인 성공",
                    content = @Content(schema = @Schema(implementation = AccessTokenResponseDTO.class)))
    })
    @PostMapping("/kakao")
    public ResponseEntity<AccessTokenResponseDTO> kakaoLogin(@RequestBody Map<String, String> request, HttpSession session) {
        String code = request.get("code");

        String kakaoAccessToken = userService.kakaoLogin(code);
        Map<String, String> response = userService.getKakaoProfile(kakaoAccessToken);

        session.setAttribute("email", response.get("email"));
        session.setAttribute("role", "USER");
        session.setAttribute("kakaoAccessToken", kakaoAccessToken);

        return ResponseEntity.ok().body(makeAccessTokenResponse(response.get("accessToken")));
    }

    @Operation(summary = "마이페이지", description = "마이페이지입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "마이페이지 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(HttpSession session) {
        String email = (String) session.getAttribute("email");
        String role = (String) session.getAttribute("role");

        UserResponseDTO res = new UserResponseDTO(email, role);
        return ResponseEntity.ok().body(res);
    }

    private AccessTokenResponseDTO makeAccessTokenResponse(String accessToken) {
        return new AccessTokenResponseDTO(accessToken);
    }
}
