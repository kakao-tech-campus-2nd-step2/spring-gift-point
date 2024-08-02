package gift.controller;

import gift.KakaoProperties;
import gift.PasswordEncoder;
import gift.model.User;
import gift.service.KakaoService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@Tag(name = "Kakao Integration", description = "APIs for Kakao integration")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;
    private final UserService userService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService, UserService userService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
        this.userService = userService;
    }

    @GetMapping("/kakao/login")
    @Operation(summary = "Kakao Login", description = "This API redirects the user to the Kakao login page.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Successfully redirected to Kakao login page."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public RedirectView kakaoLogin() {
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + kakaoProperties.getClientId() + "&redirect_uri=" + kakaoProperties.getRedirectUri() + "&scope=account_email";
        return new RedirectView(url);
    }

    @GetMapping("/kakao/callback")
    @Operation(summary = "Kakao Callback", description = "This API handles the Kakao login callback and retrieves the access token and user email.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated with Kakao."),
        @ApiResponse(responseCode = "400", description = "Invalid authorization code."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String authorizationCode, HttpSession session) {
        String accessToken = kakaoService.getAccessToken(authorizationCode);
        String email = kakaoService.getUserEmail(accessToken);

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("email", email);

        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User(null, email, PasswordEncoder.encode("1234"));
            userService.save(user);
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", accessToken);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}