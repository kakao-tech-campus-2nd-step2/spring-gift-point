package gift.kakao.login.controller;

import gift.kakao.login.service.KakaoLoginService;
import gift.user.domain.User;
import gift.user.domain.dto.LoginRequest;
import gift.user.domain.dto.LoginResponse;
import gift.user.service.UserService;
import gift.utility.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원 API")
public class KakaoLoginController {
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private String baseUrl;

    private final UserService userService;
    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(UserService userService, KakaoLoginService kakaoLoginService) {
        this.userService = userService;
        this.kakaoLoginService = kakaoLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.save(loginRequest);

        return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.findByEmail(loginRequest.email());
        if (userOptional.isPresent() && loginRequest.password().equals(userOptional.get().getPassword())) {
            String token = JwtUtil.generateToken(loginRequest.email());
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/kakao")
    @Operation(summary = "카카오 회원가입 로그인 api")
    public RedirectView showKakaoLogin(HttpServletRequest request) {
        //base url 받기
        String scheme = request.getScheme(); // http or https
        String serverName = request.getServerName(); //hostname.com
        int serverPort = request.getServerPort(); // ex) 80

        baseUrl = scheme + "://" + serverName + ":" + serverPort;

        RedirectView redirectView = new RedirectView();
        String uri = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        redirectView.setUrl(uri);
        return redirectView;
    }

    @GetMapping("/kakao/redirect")
    @Operation(summary = "카카오로부터 access code 받아서 로그인 진행")
    public RedirectView handleKakaoCallback(
        @RequestParam(value = "code") String code) {
        String accessToken = kakaoLoginService.getAccessToken(code);
        Long userId = kakaoLoginService.getUserInfo(accessToken);
        String jwtToken = userService.getJwtTokenByUserId(userId);

        String redirectUrl = baseUrl;
        String urlWithToken = redirectUrl + "?token=" + jwtToken;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(urlWithToken);
        return redirectView;
    }
}
