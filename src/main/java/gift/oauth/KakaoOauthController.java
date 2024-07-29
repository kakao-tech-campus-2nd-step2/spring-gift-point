package gift.oauth;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth")
public class KakaoOauthController {

    private final OauthService oauthService;
    private final KakaoOAuthConfigProperties configProperties;

    public KakaoOauthController(OauthService oauthService,
        KakaoOAuthConfigProperties configProperties) {
        this.oauthService = oauthService;
        this.configProperties = configProperties;
    }

    @Operation(summary = "oauth page", description = "작동하지 않습니다.")
    @GetMapping()
    public String getKakaoOauthPage(Model model) {
        model.addAttribute("redirectUri", configProperties.getRedirectUrl());
        model.addAttribute("restApiKey", configProperties.getClientId());
        return "kakaoOauth.html";
    }

    @Operation(summary = "kakaologin", description = "카카오에서 리다이렉트 토큰 받아오는 곳")
    @GetMapping("/code")
    public ResponseEntity<KakaoToken> kakoLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(oauthService.getKakaoToken(code));
    }

}
