package gift.controller;

import gift.common.properties.KakaoProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "OAuth", description = "OAuth View")
@Controller
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final KakaoProperties properties;

    public OAuthController(KakaoProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오로 로그인 주소로 리다이렉션합니다.")
    @ApiResponse(responseCode = "302")
    public RedirectView kakaoLogin() {
        return new RedirectView(properties.loginUrl() + properties.clientId());
    }
}