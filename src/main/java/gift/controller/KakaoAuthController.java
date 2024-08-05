package gift.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import gift.service.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "카카오 인증", description = "카카오 인증 관련 API")
@RestController
@RequestMapping("/kakao")
public class KakaoAuthController {
	
	private final KakaoAuthService kakaoAuthService;
	
	public KakaoAuthController(KakaoAuthService kakaoAuthService) {
		this.kakaoAuthService = kakaoAuthService;
	}
	
	@Operation(summary = "카카오 인증 리다이렉트", description = "카카오 인증 후 리다이렉트 URL을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "카카오 인증 성공")
	@GetMapping("/callback")
	public ResponseEntity<Map<String, String>> kakaoRedirect(@RequestParam("code") String authorizationCode) {
		Map<String, String> accessToken = kakaoAuthService.getAccessToken(authorizationCode);
		return ResponseEntity.status(HttpStatus.OK).body(accessToken);
	}
	
	@GetMapping("/login")
	public RedirectView kakaoLogin() {
        String url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=" + kakaoAuthService.redirectUri + "&client_id=" + kakaoAuthService.clientId;
        return new RedirectView(url);
    }
}
