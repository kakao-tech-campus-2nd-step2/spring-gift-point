package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OAuthTokenDto;
import gift.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class OauthController {

    private final OauthService oauthService;

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @Operation(summary = "Oauth 로그인")
    @GetMapping("/oauth/kakao")
    public void kakaologin(HttpServletResponse response) throws IOException {
        oauthService.redirectKakaoLogin(response);
    }

    @GetMapping
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        try {
            String accessToken = oauthService.handleKakaoCallback(code);
            return ResponseEntity.status(HttpStatus.OK).body(accessToken);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JSON processing error");
        }
    }

    @Operation(summary = "Oauth 회원가입")
    @PostMapping("/oauth/register")
    public ResponseEntity<String> register(@RequestBody OAuthTokenDto oAuthTokenDto) {
        try {
            oauthService.registerUser(oAuthTokenDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("OAuth를 사용하여 유저 생성 완료");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JSON processing error");
        }
    }
}
