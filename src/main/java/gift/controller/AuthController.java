package gift.controller;

import gift.Login;
import gift.dto.LoginMember;
import gift.dto.request.AuthRequest;
import gift.dto.response.AuthResponse;
import gift.dto.response.PointResponse;
import gift.service.AuthService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Account", description = "회원가입, 로그인 등 사용자 계정과 관련된 API Controller")
public class AuthController {

    @Value("${kakao.get-code.url}")
    private String getCodeUrl;
    private final AuthService authService;
    private final OrderService orderService;

    private String baseUrl;

    public AuthController(AuthService authService, OrderService orderService) {
        this.authService = authService;
        this.orderService = orderService;
    }

    @PostMapping("/register")
    @Operation(summary = "이메일 회원가입 api")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    public ResponseEntity<AuthResponse> memberRegister(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.addMember(authRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "이메일 로그인 api")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    public ResponseEntity<AuthResponse> memberLogin(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
    }

    @PostMapping("/kakao")
    @Operation(summary = "카카오 회원가입 및 로그인 api")
    @ApiResponse(responseCode = "200", description = "카카오 로그인 성공")
    public RedirectView kakaoLoginRedirect(HttpServletRequest request) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(getCodeUrl);

        // save client url
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        baseUrl = scheme + "://" + serverName + ":" + serverPort + contextPath;

        return redirectView;
    }

    @Hidden
    @GetMapping("/kakao/redirect")
    public RedirectView kakaoLogin(@RequestParam("code") String code) {

        String redirectTo = baseUrl + "?token=" + authService.kakaoLogin(code);
//        return new ResponseEntity<>(authService.kakaoLogin(code), HttpStatus.OK);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectTo);

        return redirectView;
    }

    @GetMapping("/point")
    @Operation(summary = "회원 포인트 조회 api")
    @ApiResponse(responseCode = "200", description = "회원 포인트 조회 성공")
    public ResponseEntity<PointResponse> getPoint(@Login LoginMember member) {
        return new ResponseEntity<>(orderService.getMemberPoint(member.getId()), HttpStatus.OK);
    }
}
