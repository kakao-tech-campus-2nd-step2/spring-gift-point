package gift.product.controller.auth;

import gift.product.dto.auth.AccessTokenDto;
import gift.product.dto.auth.AccountDto;
import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.auth.MemberDto;
import gift.product.dto.auth.OAuthJwt;
import gift.product.exception.ExceptionResponse;
import gift.product.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "auth", description = "회원가입, 로그인, 탈퇴 관련 API")
public class AuthController {

    private static final String KAKAO_AUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_UNLINK_USER_URL = "https://kapi.kakao.com/v1/user/unlink";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccessTokenDto.class))),
        @ApiResponse(responseCode = "409", description = "회원 가입 실패(이미 존재하는 이메일)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = gift.product.exception.ExceptionResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<AccessTokenDto> registerMember(
        @RequestBody MemberDto memberDto) {
        AccessTokenDto accessTokenDto = authService.register(memberDto);

        return ResponseEntity.ok(accessTokenDto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccessTokenDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> loginMember(@RequestBody AccountDto accountDto) {
        AccessTokenDto accessTokenDto = authService.login(accountDto);

        return ResponseEntity.ok(accessTokenDto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "리디렉션 성공"),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/login/kakao")
    public ResponseEntity<Void> loginKakao() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authService.getKakaoAuthCodeUrl()));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카카오 로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccessTokenDto.class))),
        @ApiResponse(responseCode = "401", description = "사용자 인증 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/login/kakao/callback")
    public ResponseEntity<AccessTokenDto> getKakaoJwt(@RequestParam(name = "code") String code) {
        OAuthJwt oAuthJwt = authService.getOAuthToken(code, KAKAO_AUTH_TOKEN_URL);
        AccessTokenDto accessTokenDto = authService.registerKakaoMember(oAuthJwt,
            KAKAO_USER_INFO_URL);
        return ResponseEntity.ok(accessTokenDto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "401", description = "실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/login/kakao/unlink")
    public ResponseEntity<Long> unlinkKakaoAccount(HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        return ResponseEntity.ok(authService.unlinkKakaoAccount(loginMemberIdDto,
            KAKAO_UNLINK_USER_URL));
    }

    private LoginMemberIdDto getLoginMember(HttpServletRequest request) {
        return new LoginMemberIdDto((Long) request.getAttribute("id"));
    }
}
