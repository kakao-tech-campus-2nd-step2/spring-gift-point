package gift.user.controller;

import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gift.core.api.kakao.KakaoRestClient;
import gift.dto.request.LoginRequest;
import gift.dto.request.SignupRequest;
import gift.user.service.OAuthservice;
import gift.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserLoginController {
	private final UserService userService;
	private final OAuthservice oAuthService;

	public UserLoginController(UserService userService, OAuthservice oAuthService, KakaoRestClient kakaoRestClient,
		CacheManager cacheManager) {
		this.userService = userService;
		this.oAuthService = oAuthService;
	}

	// email과 패스워드를 입력하면, 해당 유저의 정보를 JWT access token으로 반환
	@PostMapping("/api/members/login")
	public String login(@RequestBody LoginRequest loginRequest) {
		return userService.loginUser(loginRequest.email(), loginRequest.password());
	}

	@PostMapping("/api/members/register")
	public void signup(@RequestBody SignupRequest signupRequest) {
		userService.registerUser(signupRequest);
	}

	@GetMapping("/oauth/kakao/redirect")
	public ResponseEntity<String> login(@RequestParam("code") String code,
			HttpServletResponse response) {
		String email = oAuthService.authenticate(code);
		String token = userService.loginOauth2User(email);

		response.setHeader("Authorization", "Bearer "+token);
		return ResponseEntity.ok("login success");
	}

	@GetMapping("/oauth/kakao")
	public ResponseEntity<String> login() {
		return ResponseEntity.ok("login success");
	}
}
