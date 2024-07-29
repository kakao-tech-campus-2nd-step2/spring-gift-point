package gift.auth.controller;

import gift.auth.domain.KakaoToken.kakaoToken;
import gift.auth.domain.Login;
import gift.auth.domain.Token;
import gift.auth.service.LoginService;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 관련 서비스")
@RestController
@RequestMapping("/api/login")
public class LoginController {


    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary  = "이메일로 로그인", description  = "이메일과 비밀번호로 로그인해 JWT토큰을 반환한다.")
    @PostMapping
    public SingleResult<Token> Login(@Valid @RequestBody Login login) {
        return new SingleResult<>(loginService.Login(login));
    }

    @Operation(summary  = "소셜 로그인(카카오)", description  = "카카오에서 발급한 엑세스 토큰으로 로그인해 JWT토큰을 반환한다.")
    @PostMapping("/kakao")
    public SingleResult<Token> KakaoLogin(@Valid @RequestBody kakaoToken token) {
        return new SingleResult<>(loginService.kakaoLogin(token));
    }
}
