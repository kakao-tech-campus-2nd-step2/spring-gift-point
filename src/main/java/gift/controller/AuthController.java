package gift.controller;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomArgumentNotValidException;
import gift.exception.customException.CustomDuplicateException;
import gift.exception.customException.CustomException;
import gift.exception.customException.PassWordMissMatchException;
import gift.model.dto.UserDTO;
import gift.model.form.UserForm;
import gift.oauth.KakaoOAuthService;
import gift.service.JwtProvider;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Api", description = "로그인 및 회원가입 관련 Api")
@RestController
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final KakaoOAuthService kakaoOAuthService;

    public AuthController(UserService userService, JwtProvider jwtProvider,
        KakaoOAuthService kakaoOAuthService) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @Operation(summary = "카카오 소셜 로그인", responses = @ApiResponse(responseCode = "200", description = "로그인 성공시 토큰 반환"))
    @GetMapping("/api/members/login/kakao")
    public ResponseEntity<?> getKakaoLoginPage() {
        var uri = kakaoOAuthService.getKakaoLoginPage();
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(uri).build();
    }

    @Operation(summary = "카카오 소셜 로그인 리다이렉트")
    @GetMapping
    public ResponseEntity<?> handleKakaoLoginRequest(@RequestParam("code") String code) {
        var token = kakaoOAuthService.requestToken(code);
        Long kakaoId = kakaoOAuthService.getKakaoId(token.accessToken());
        UserDTO userDTO = userService.findByKakaoId(kakaoId, token);
        String newToken = jwtProvider.generateToken(userDTO);
        return ResponseEntity.ok(newToken);
    }

    @Operation(summary = "로그인", responses = @ApiResponse(responseCode = "200", description = "로그인 성공시 토큰 반환"))
    @PostMapping("/api/members/login")
    public ResponseEntity<?> handleLoginRequest(@Valid @RequestBody UserForm userForm,
        BindingResult result) throws MethodArgumentNotValidException {
        checkLoginUser(userForm, result);
        return ResponseEntity.ok(
            jwtProvider.generateToken(userService.findByEmail(userForm.getEmail())));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/api/members/register")
    public ResponseEntity<?> handleSignUpRequest(@Valid @RequestBody UserForm userForm,
        BindingResult result) throws CustomException, CustomArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException(result, ErrorCode.BAD_REQUEST);
        }
        if (userService.existsEmail(userForm.getEmail())) {
            result.rejectValue("email", "", ErrorCode.DUPLICATE_EMAIL.getMessage());
            throw new CustomDuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
        Long id = userService.insertUser(userForm);
        return ResponseEntity.ok(
            jwtProvider.generateToken(userService.findByEmail(userForm.getEmail())));
    }

    @Operation(hidden = true)
    public void checkLoginUser(UserForm userForm, BindingResult result)
        throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException(result, ErrorCode.BAD_REQUEST);
        }
        if (!userService.existsEmail(userForm.getEmail())) {
            result.rejectValue("email", "", ErrorCode.EMAIL_NOT_FOUND.getMessage());
            throw new CustomArgumentNotValidException(result, ErrorCode.EMAIL_NOT_FOUND);
        }
        if (!userService.isPasswordMatch(userForm)) {
            result.rejectValue("password", "", ErrorCode.PASSWORD_MISMATCH.getMessage());
            throw new PassWordMissMatchException(result, ErrorCode.PASSWORD_MISMATCH);
        }
    }

}