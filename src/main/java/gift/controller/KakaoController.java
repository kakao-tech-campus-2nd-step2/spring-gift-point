package gift.controller;

import gift.config.KakaoProperties;
import gift.domain.Token;
import gift.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao/login")
@Tag(name = "Kakao", description = "카카오 API")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("")
    @Operation(summary = "카카오 인증 코드", description = "카카오 인증 코드를 제공하는 callback path로 리다이렉트")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String codeUrl = kakaoService.getCode();
        response.sendRedirect(codeUrl);
    }

    @GetMapping("/callback")
    @Operation(summary = "카카오 로그인", description = "카카오 인증 토큰을 얻어서 해당 이메일로 로그인")
    public ResponseEntity<?> loginByKakaoEmail(@RequestParam("code") String code) {
       Token token = kakaoService.login(code);
       return ResponseEntity.ok(token);
    }

}

