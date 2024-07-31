package gift.controller;

import gift.auth.dto.KakaoProperties;
import gift.domain.Role;
import gift.domain.User;
import gift.dto.common.apiResponse.ApiResponseBody.SuccessBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import gift.dto.requestdto.UserLoginRequestDTO;
import gift.dto.requestdto.UserSignupRequestDTO;
import gift.dto.responsedto.UserTokenResponseDTO;
import gift.service.AuthService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(name = "인증 api", description = "인증 api입니다")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/api/auth/register")
    @ApiResponse(responseCode = "200", description = "일반 회원가입 성공")
    public ResponseEntity<SuccessBody<UserTokenResponseDTO>> signUp(
        @Valid @RequestBody UserSignupRequestDTO userSignupRequestDTO) {
        userService.join(userSignupRequestDTO);
        UserTokenResponseDTO userTokenResponseDTO = authService.register(userSignupRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.CREATED, "회원가입에 성공했습니다.",
            userTokenResponseDTO);
    }

    @ResponseBody
    @PostMapping("/api/auth/login")
    @ApiResponse(responseCode = "200", description = "일반 로그인 성공")
    public ResponseEntity<SuccessBody<UserTokenResponseDTO>> login(
        @Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        User user = userService.findByEmail(userLoginRequestDTO);
        UserTokenResponseDTO userTokenResponseDTO = authService.login(user, userLoginRequestDTO, null);
        return ApiResponseGenerator.success(HttpStatus.ACCEPTED, "로그인에 성공했습니다.",
            userTokenResponseDTO);
    }

    @GetMapping("/api/oauth/redirect")
    public String redirect() {
        KakaoProperties properties = authService.getProperties();
        String clientId = properties.clientId();
        String redirectUri = properties.redirectUrl();
        return "redirect:https://kauth.kakao.com/oauth/authorize?response_type=code"
            + "&client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&scope=account_email";
    }

    @ResponseBody
    @GetMapping("/")
    @ApiResponse(responseCode = "200", description = "인가 코드 추출 성공")
    public ResponseEntity<SuccessBody<String>> getAuthorizationCode(
        @RequestParam("code") String code
    ) {
        return ApiResponseGenerator.success(HttpStatus.OK, "인가 코드 추출 성공", code);
    }

    @ResponseBody
    @PostMapping("/api/oauth/login")
    @ApiResponse(responseCode = "200", description = "카카오 로그인 성공")
    public ResponseEntity<SuccessBody<UserTokenResponseDTO>> kakaoLogin(
        @RequestParam("code") String code
    ) {
        String accessToken = authService.getAccessToken(code);
        String userEmail = authService.getUserEmail(accessToken);
        Optional<User> user = userService.findByEmail(userEmail);

        UserTokenResponseDTO userTokenResponseDTO = user.map(existUser ->
            authService.login(existUser, new UserLoginRequestDTO(userEmail, existUser.getPassword()), accessToken)
        ).orElseGet(() -> {
            User joinedUser = userService.join(
                new UserSignupRequestDTO(userEmail, "kakao", Role.ADMIN.getRole()));
            return authService.login(joinedUser, new UserLoginRequestDTO(userEmail, "kakao"), accessToken);
        });

        return ApiResponseGenerator.success(HttpStatus.OK, "jwt 토큰 발급 성공", userTokenResponseDTO);
    }

}
