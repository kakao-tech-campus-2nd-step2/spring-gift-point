package gift.controller;

import gift.common.exception.InvalidTokenException;
import gift.dto.KakaoAccessToken;
import gift.dto.KakaoProperties;
import gift.dto.KakaoUserInfo;
import gift.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@Tag(name = "Kakao API", description = "카카오 로그인 관련 API")
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoProperties kakaoProperties;

    public KakaoController(KakaoService kakaoService, KakaoProperties kakaoProperties) {
        this.kakaoService = kakaoService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인 폼", description = "카카오 로그인 폼을 보여줍니다.")
    public String loginForm(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.getClientId());
        model.addAttribute("kakaoRedirectUrl", kakaoProperties.getRedirectUrl());
        return "login";
    }

    @GetMapping("/direct/login")
    @Operation(summary = "카카오 바로 로그인", description = "카카오 로그인 페이지로 리다이렉트합니다.")
    public String loginForm() {
        return "redirect:" + kakaoService.getKakaoLogin();
    }

    @GetMapping("/")
    @Operation(summary = "카카오 콜백", description = "카카오 로그인 후 리다이렉트되는 콜백 URL입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public String kakaoCallback(@RequestParam(required = false) String code, Model model,
        HttpSession session) {
        if (code == null) {
            throw new InvalidTokenException("Authorization code가 없습니다.");
        }

        KakaoAccessToken tokenResponse = kakaoService.getAccessToken(code);
        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new InvalidTokenException("잘못된 access token");
        }
        String accessToken = tokenResponse.getAccessToken();

        KakaoUserInfo userInfo = kakaoService.getUserInfo(accessToken);

        session.setAttribute("userInfo", userInfo);
        session.setAttribute("accessToken", accessToken);

        System.out.println("Access Token: " + accessToken);

        return "redirect:/user";
    }

    @GetMapping("/user")
    @Operation(summary = "사용자 정보", description = "카카오 로그인 후 사용자 정보를 보여줍니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = KakaoUserInfo.class))}),
        @ApiResponse(responseCode = "404", description = "사용자가 존재하지 않음")
    })
    public String user(Model model, @SessionAttribute("userInfo") KakaoUserInfo userInfo) {
        if (userInfo == null) {
            throw new RuntimeException("사용자 정보를 찾을 수 없습니다.");
        }

        model.addAttribute("userInfo", userInfo);
        return "user";
    }

}
