package gift.user.controller;

import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gift.core.api.kakao.KakaoRestClient;
import gift.dto.request.UserDetails;
import gift.user.service.OAuthservice;
import gift.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;


@Tag(name = "User", description = "유저 관련 API")
@RestController
public class UserRestController {
	private final UserService userService;
	private final OAuthservice oAuthService;

	public UserRestController(UserService userService, OAuthservice oAuthService, KakaoRestClient kakaoRestClient,
		CacheManager cacheManager) {
		this.userService = userService;
		this.oAuthService = oAuthService;
	}

	@Operation(summary = "카카오 로그인", description = "카카오 로그인 페이지로 이동")
	@GetMapping("/oauth/kakao/redirect")
	public ResponseEntity<String> login(@RequestParam("code") String code,
			HttpServletResponse response) {
		String email = oAuthService.authenticate(code);
		String token = userService.loginOauth2User(email);

		response.setHeader("Authorization", "Bearer "+token);
		return ResponseEntity.ok(token);
	}

	@Operation(summary = "포인트 조회", description = "유저의 포인트 조회")
	@GetMapping("/api/points")
	public ResponseEntity<Long> getPoints(@RequestAttribute("UserDetails")UserDetails userDetails) {
		return ResponseEntity.ok(userService.getPoints(userDetails.userId()));
	}

	@Operation(summary = "포인트 적립", description = "유저의 포인트를 10000포인트 적립하는 테스트 API")
	@GetMapping("/api/test/points")
	public ResponseEntity<Long> addPoints(@RequestAttribute("UserDetails")UserDetails userDetails) {
		return ResponseEntity.ok(userService.addPoints(userDetails.userId(), 10000L));
	}
}
