package gift.controller.auth;

import gift.controller.member.MemberResponse;
import gift.controller.response.ApiResponseBody;
import gift.controller.response.ApiResponseBuilder;
import gift.exception.AuthorizationCodeException;
import gift.exception.UnauthorizedException;
import gift.service.AuthService;
import gift.service.KakaoTokenService;
import gift.service.MemberService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final KakaoTokenService kakaoTokenService;

    public AuthController(AuthService authService, MemberService memberService,
        KakaoTokenService kakaoTokenService) {
        this.authService = authService;
        this.memberService = memberService;
        this.kakaoTokenService = kakaoTokenService;
    }

    public static void validateUserOrAdmin(LoginResponse member, UUID id) {
        if (!member.id().equals(id) && !member.isAdmin()) {
            throw new UnauthorizedException();
        }
    }

    @PostMapping("api/members/login")
    @Operation(summary = "login", description = "로그인 후 토큰 반환")
    public ResponseEntity<ApiResponseBody<Token>> login(@RequestBody LoginRequest member) {
        Token token = authService.login(member);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.token());
        return new ApiResponseBuilder<Token>()
            .httpStatus(HttpStatus.OK)
            .headers(headers)
            .data(token)
            .messages("로그인")
            .build();
    }

    @GetMapping("/authorize")
    @Operation(summary = "authorize", description = "카카오 서버에서 인가코드 획득 및 리다이렉션")
    public void getAuthorizationCode(HttpServletResponse response) {
        try {
            response.sendRedirect(kakaoTokenService.getAuthorizationUrl());
        } catch (Exception e) {
            throw new AuthorizationCodeException();
        }
    }

    @GetMapping("/kakao/login")
    @Operation(summary = "kakaoLogin", description = "인가코드를 통해 토큰 발급 및 DB 저장, 카카오 계정의 이메일로 로그인(미 가입시 예외 발생)")
    public ResponseEntity<ApiResponseBody<Token>> loginWithKakao(@RequestParam String code) {
        KakaoTokenResponse kakaoToken = kakaoTokenService.getKakaoToken(code);
        MemberResponse member = memberService.findByEmail(
            kakaoTokenService.getMemberInfo(kakaoToken).email());
        kakaoTokenService.save(member.id(), kakaoToken);
        Token token = new Token(JwtUtil.generateToken(member.id(), member.email()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.token());
        return new ApiResponseBuilder<Token>()
            .httpStatus(HttpStatus.OK)
            .headers(headers)
            .data(token)
            .messages("카카오 로그인")
            .build();
    }
}