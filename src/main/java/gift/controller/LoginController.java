package gift.controller;

import gift.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Login(로그인)", description = "Oauth(KaKao) Login관련 API입니다.")
public class LoginController {

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/code")
    public ResponseEntity<String> LoginAndGetAccessToken(String code) {
        String accessToken = loginService.oauthLogin(code);

        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

}
