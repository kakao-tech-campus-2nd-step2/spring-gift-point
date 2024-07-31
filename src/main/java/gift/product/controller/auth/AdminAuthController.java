package gift.product.controller.auth;

import gift.product.dto.auth.AccessTokenDto;
import gift.product.dto.auth.AccountDto;
import gift.product.dto.auth.JwtResponse;
import gift.product.dto.auth.MemberDto;
import gift.product.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Hidden
@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    public static final String ACCESS_TOKEN = "accessToken";
    private static final String REDIRECT_ADMIN_LOGIN_PROCESSING = "redirect:/admin/login/process";
    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "admin/loginForm";
    }

    @PostMapping("/login")
    public String login(AccountDto accountDto, RedirectAttributes redirectAttributes) {
        AccessTokenDto accessTokenDto = authService.login(accountDto);
        redirectAttributes.addAttribute(ACCESS_TOKEN, accessTokenDto.accessToken());

        return REDIRECT_ADMIN_LOGIN_PROCESSING;
    }

    @GetMapping("/login/process")
    public String loginProcess(@RequestParam(ACCESS_TOKEN) String accessToken,
        Model model) {
        model.addAttribute(ACCESS_TOKEN, accessToken);

        return "admin/loginProcess";
    }

    @PostMapping("login/process")
    @ResponseBody
    public ResponseEntity<Void> loginSuccess(@RequestParam(ACCESS_TOKEN) String accessToken,
        HttpServletResponse response) {
        addAccessTokenCookieInResponse(accessToken, response);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/admin/products"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    private void addAccessTokenCookieInResponse(String accessToken,
        HttpServletResponse response) {
        Cookie cookie = new Cookie(ACCESS_TOKEN, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/admin");
        response.addCookie(cookie);
    }
}
