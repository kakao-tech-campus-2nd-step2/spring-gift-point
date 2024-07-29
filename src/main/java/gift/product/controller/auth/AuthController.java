package gift.product.controller.auth;

import gift.product.ProblemDetailResponse;
import gift.product.dto.auth.JwtResponse;
import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.auth.MemberDto;
import gift.product.dto.auth.OAuthJwt;
import gift.product.dto.auth.RegisterSuccessResponse;
import gift.product.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterSuccessResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetailResponse.class)))
    })
    @PostMapping("/members/register")
    public ResponseEntity<RegisterSuccessResponse> registerMember(
        @RequestBody MemberDto memberDto) {
        authService.register(memberDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new RegisterSuccessResponse("회원가입이 완료되었습니다."));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "403", description = "로그인 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetailResponse.class)))
    })
    @PostMapping("/members/login")
    public ResponseEntity<JwtResponse> loginMember(@RequestBody MemberDto memberDto) {
        JwtResponse jwtResponse = authService.login(memberDto);

        return ResponseEntity.ok(jwtResponse);
    }

    @ApiResponse(responseCode = "303", description = "카카오 로그인 진행 후 특정 URL로 리다이렉트 되어 아래의 응답을 받음 (전달 형식은 임시)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class)))
    @GetMapping("/members/login/kakao")
    public ResponseEntity<Void> loginKakao() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authService.getKakaoAuthCodeUrl()));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @Hidden
    @GetMapping
    public ResponseEntity<JwtResponse> getKakaoJwt(@RequestParam(name = "code") String code) {
        OAuthJwt oAuthJwt = authService.getOAuthToken(code, KAKAO_AUTH_TOKEN_URL);
        JwtResponse jwtResponse = authService.registerKakaoMember(oAuthJwt,
            KAKAO_USER_INFO_URL);
        return ResponseEntity.ok(jwtResponse);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "403", description = "로그인 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetailResponse.class)))
    })
    @PostMapping("/members/login/kakao/unlink")
    public ResponseEntity<Long> unlinkKakaoAccount(HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        return ResponseEntity.ok(authService.unlinkKakaoAccount(loginMemberIdDto,
            KAKAO_UNLINK_USER_URL));
    }

    private LoginMemberIdDto getLoginMember(HttpServletRequest request) {
        return new LoginMemberIdDto((Long) request.getAttribute("id"));
    }
}
