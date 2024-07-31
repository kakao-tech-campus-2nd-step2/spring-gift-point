package gift.web.controller.api;

import gift.service.LoginService;
import gift.web.dto.response.LoginResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/oauth2/kakao")
    public LoginResponse kakaoLogin(String code){
        return loginService.kakaoLogin(code);
    }

}
